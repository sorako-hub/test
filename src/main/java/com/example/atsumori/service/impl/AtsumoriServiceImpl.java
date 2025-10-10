package com.example.atsumori.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    /**
     * 生き物全件取得
     */
    @Override
    public List<Creatures> findAllCreatures() {
        List<Creatures> creatures = atsumoriMapper.selectAll();

        for (Creatures c : creatures) {
            splitAppearancePeriods(c);
        }

        return creatures;
    }

    /**
     * 名前で生き物検索
     */
    @Override
    public Creatures findByNameCreature(String name) {
        Creatures creature = atsumoriMapper.selectByName(name);
        if (creature == null) {
            return null;
        }

        splitAppearancePeriods(creature);
        return creature;
    }

    /**
     * 月・半球で出現する生き物検索（ログイン不要）
     */
    @Override
    public List<Creatures> findByAppearance(int month, String hemisphere) {
        if (!"北半球".equals(hemisphere) && !"南半球".equals(hemisphere)) {
            throw new IllegalArgumentException("半球は必ず「北半球」か「南半球」を指定してください");
        }

        List<Creatures> allCreatures = atsumoriMapper.selectAll();
        List<Creatures> result = new ArrayList<>();

        for (Creatures creature : allCreatures) {
            splitAppearancePeriods(creature);

            List<AppearancePeriod> periods = creature.getAppearancePeriods();
            if (Objects.isNull(periods) || periods.isEmpty()) continue;

            boolean appearsInGivenMonth = periods.stream()
                .anyMatch(p -> p.getHemisphere().equals(hemisphere) &&
                    (
                        (p.getStartMonth() <= p.getEndMonth() && month >= p.getStartMonth() && month <= p.getEndMonth())
                        ||
                        (p.getStartMonth() > p.getEndMonth() && (month >= p.getStartMonth() || month <= p.getEndMonth()))
                    )
                );

            if (appearsInGivenMonth) {
                result.add(creature);
            }
        }

        return result;
    }

    /**
     * 釣った魚（ログインユーザーごと）
     */
    @Override
    public List<Creatures> findCaughtFishByUserId(Long userId) {
        List<Creatures> caughtFish = atsumoriMapper.selectCaughtFish(userId);

        for (Creatures c : caughtFish) {
            splitAppearancePeriods(c);
        }

        return caughtFish;
    }

    /**
     * 釣ってない魚（ログインユーザーごと）
     */
    @Override
    public List<Creatures> findUncaughtFishByUserId(Long userId) {
        List<Creatures> uncaughtFish = atsumoriMapper.selectUncaughtFish(userId);

        for (Creatures c : uncaughtFish) {
            splitAppearancePeriods(c);
        }

        return uncaughtFish;
    }

    /**
     * 月・半球で釣った魚（ログインユーザーごと）
     */
    @Override
    public List<Creatures> findCaughtFishByAppearance(Long userId, int month, String hemisphere) {
        List<Creatures> caughtFish = atsumoriMapper.selectCaughtFishByAppearance(userId, month, hemisphere);

        for (Creatures c : caughtFish) {
            splitAppearancePeriods(c);
        }

        return caughtFish;
    }

    /**
     * 月・半球でまだ釣っていない魚（ログインユーザーごと）
     */
    @Override
    public List<Creatures> findUncaughtFishByAppearance(Long userId, int month, String hemisphere) {
        List<Creatures> uncaughtFish = atsumoriMapper.selectUncaughtFishByAppearance(userId, month, hemisphere);

        for (Creatures c : uncaughtFish) {
            splitAppearancePeriods(c);
        }

        return uncaughtFish;
    }

    /**
     * 出現期間を北半球／南半球に分けてセットする共通処理
     */
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
}
