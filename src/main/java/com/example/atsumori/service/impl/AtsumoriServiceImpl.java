package com.example.atsumori.service.impl;

import java.util.ArrayList;
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

    @Override
    public List<Creatures> findAllCreatures() {
        List<Creatures> creatures = atsumoriMapper.selectAll();

        // 出現期間を北半球・南半球で分割
        for (Creatures c : creatures) {
            List<AppearancePeriod> northern = new ArrayList<>();
            List<AppearancePeriod> southern = new ArrayList<>();
            if (c.getAppearancePeriods() != null) {
                for (AppearancePeriod p : c.getAppearancePeriods()) {
                    if ("北半球".equals(p.getHemisphere())) {
                        northern.add(p);
                    } else if ("南半球".equals(p.getHemisphere())) {
                        southern.add(p);
                    }
                }
            }
            c.setNorthernAppearancePeriods(northern);
            c.setSouthernAppearancePeriods(southern);
        }

        return creatures;
    }

    @Override
    public Creatures findByNameCreature(String name) {
        Creatures creature = atsumoriMapper.selectByName(name);
        if (creature == null) {
            return null;
        }

        List<AppearancePeriod> allPeriods = creature.getAppearancePeriods();

        if (allPeriods == null || allPeriods.isEmpty()) {
            creature.setNorthernAppearancePeriods(List.of());
            creature.setSouthernAppearancePeriods(List.of());
            return creature;
        }

        List<AppearancePeriod> northern = allPeriods.stream()
            .filter(p -> "北半球".equals(p.getHemisphere()))
            .collect(Collectors.toList());

        List<AppearancePeriod> southern = allPeriods.stream()
            .filter(p -> "南半球".equals(p.getHemisphere()))
            .collect(Collectors.toList());

        creature.setNorthernAppearancePeriods(northern);
        creature.setSouthernAppearancePeriods(southern);

        return creature;
    }


    @Override
    public List<Creatures> findByAppearance(int month, String hemisphere) {
        if (!"北半球".equals(hemisphere) && !"南半球".equals(hemisphere)) {
            throw new IllegalArgumentException("半球は必ず「北半球」か「南半球」を指定してください");
        }

        // すべての生き物を取得（出現期間も含める）
        List<Creatures> allCreatures = atsumoriMapper.selectAll();

        List<Creatures> result = new ArrayList<>();

        for (Creatures creature : allCreatures) {
            List<AppearancePeriod> allPeriods = creature.getAppearancePeriods();

            if (allPeriods == null || allPeriods.isEmpty()) {
                creature.setNorthernAppearancePeriods(List.of());
                creature.setSouthernAppearancePeriods(List.of());
                continue;
            }

            // 北半球・南半球の出現期間をセット（これは全期間）
            List<AppearancePeriod> northPeriods = allPeriods.stream()
                    .filter(p -> "北半球".equals(p.getHemisphere()))
                    .collect(Collectors.toList());

            List<AppearancePeriod> southPeriods = allPeriods.stream()
                    .filter(p -> "南半球".equals(p.getHemisphere()))
                    .collect(Collectors.toList());

            creature.setNorthernAppearancePeriods(northPeriods);
            creature.setSouthernAppearancePeriods(southPeriods);

            // ✅ 指定された月と半球で出現するかどうかチェック
            boolean appearsInGivenMonth = allPeriods.stream()
                    .anyMatch(p -> p.getHemisphere().equals(hemisphere) &&
                                   p.getStartMonth() <= month &&
                                   p.getEndMonth() >= month);

            if (appearsInGivenMonth) {
                result.add(creature); // 出現する生き物だけをリストに追加
            }
        }

        return result;
    }
}