package com.example.atsumori.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.atsumori.entity.AppearancePeriod;
import com.example.atsumori.entity.Creatures;
import com.example.atsumori.repository.AtsumoriMapper;
import com.example.atsumori.service.AtsumoriService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AtsumoriServiceImpl implements AtsumoriService {

    private final AtsumoriMapper atsumoriMapper;

    /** 全生き物 */
    @Override
    public List<Creatures> findAllCreatures() {
        List<Creatures> creatures = atsumoriMapper.selectAll();
        creatures.forEach(this::splitAppearancePeriods);
        return creatures;
    }

    /** タイプ別生き物一覧 */
    @Override
    public List<Creatures> findCreaturesByType(String type) {
        List<Creatures> creatures = atsumoriMapper.selectByType(type);
        creatures.forEach(this::splitAppearancePeriods);
        return creatures;
    }

    /** 名前とタイプで1件取得 */
    @Override
    public Creatures findByNameAndType(String name, String type) {
        Creatures creature = atsumoriMapper.selectByNameAndType(name, type);
        if (creature != null) {
            splitAppearancePeriods(creature);
        }
        return creature;
    }

    /** 月・半球・タイプで出現生物を取得（ログイン不要） */
    @Override
    public List<Creatures> findByAppearanceAndType(int month, String hemisphere, String type) {
        List<Creatures> creatures = atsumoriMapper.selectByType(type);
        List<Creatures> result = new ArrayList<>();

        for (Creatures creature : creatures) {
            splitAppearancePeriods(creature);
            List<AppearancePeriod> periods = creature.getAppearancePeriods();

            if (periods == null || periods.isEmpty()) continue;

            boolean appearsInMonth = periods.stream()
                .anyMatch(p -> p.getHemisphere().equals(hemisphere) &&
                    (
                        (p.getStartMonth() <= p.getEndMonth() && month >= p.getStartMonth() && month <= p.getEndMonth()) ||
                        (p.getStartMonth() > p.getEndMonth() && (month >= p.getStartMonth() || month <= p.getEndMonth()))
                    )
                );

            if (appearsInMonth) {
                result.add(creature);
            }
        }

        return result;
    }

    /** 取得済み（タイプ別） */
    @Override
    public List<Creatures> findCaughtCreaturesByType(Long userId, String type) {
        List<Creatures> creatures = atsumoriMapper.selectCaughtCreaturesByType(userId, type);
        creatures.forEach(this::splitAppearancePeriods);
        return creatures;
    }

    /** 未取得（タイプ別） */
    @Override
    public List<Creatures> findUncaughtCreaturesByType(Long userId, String type) {
        List<Creatures> creatures = atsumoriMapper.selectUncaughtCreaturesByType(userId, type);
        creatures.forEach(this::splitAppearancePeriods);
        return creatures;
    }

    /** 取得済み（タイプ＋月＋半球） */
    @Override
    public List<Creatures> findCaughtCreaturesByAppearance(Long userId, int month, String hemisphere, String type) {
        List<Creatures> creatures = atsumoriMapper.selectCaughtCreaturesByType(userId, type);
        return filterByAppearance(creatures, month, hemisphere);
    }

    /** 未取得（タイプ＋月＋半球） */
    @Override
    public List<Creatures> findUncaughtCreaturesByAppearance(Long userId, int month, String hemisphere, String type) {
        List<Creatures> creatures = atsumoriMapper.selectUncaughtCreaturesByType(userId, type);
        return filterByAppearance(creatures, month, hemisphere);
    }

    /** 出現期間を北・南半球に分割 */
    private void splitAppearancePeriods(Creatures creature) {
        if (creature == null || creature.getAppearancePeriods() == null) {
            creature.setNorthernAppearancePeriods(Collections.emptyList());
            creature.setSouthernAppearancePeriods(Collections.emptyList());
            return;
        }

        List<AppearancePeriod> periods = creature.getAppearancePeriods();

        List<AppearancePeriod> northern = periods.stream()
            .filter(p -> "北半球".equals(p.getHemisphere()))
            .collect(Collectors.toList());

        List<AppearancePeriod> southern = periods.stream()
            .filter(p -> "南半球".equals(p.getHemisphere()))
            .collect(Collectors.toList());

        creature.setNorthernAppearancePeriods(northern);
        creature.setSouthernAppearancePeriods(southern);
    }

    /** 月・半球でフィルタリング */
    private List<Creatures> filterByAppearance(List<Creatures> creatures, int month, String hemisphere) {
        List<Creatures> result = new ArrayList<>();
        for (Creatures creature : creatures) {
            splitAppearancePeriods(creature);
            List<AppearancePeriod> periods = creature.getAppearancePeriods();

            if (periods == null || periods.isEmpty()) continue;

            boolean appearsInMonth = periods.stream()
                .anyMatch(p -> p.getHemisphere().equals(hemisphere) &&
                    (
                        (p.getStartMonth() <= p.getEndMonth() && month >= p.getStartMonth() && month <= p.getEndMonth()) ||
                        (p.getStartMonth() > p.getEndMonth() && (month >= p.getStartMonth() || month <= p.getEndMonth()))
                    )
                );

            if (appearsInMonth) {
                result.add(creature);
            }
        }
        return result;
    }
}
