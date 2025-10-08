package com.example.atsumori.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
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

        for (Creatures c : creatures) {
            c.setNorthernAppearancePeriods(mergePeriods(c.getAppearancePeriods(), "北半球"));
            c.setSouthernAppearancePeriods(mergePeriods(c.getAppearancePeriods(), "南半球"));
        }

        return creatures;
    }

    @Override
    public Creatures findByNameCreature(String name) {
        Creatures creature = atsumoriMapper.selectByName(name);
        if (creature == null) return null;

        creature.setNorthernAppearancePeriods(mergePeriods(creature.getAppearancePeriods(), "北半球"));
        creature.setSouthernAppearancePeriods(mergePeriods(creature.getAppearancePeriods(), "南半球"));

        return creature;
    }

    @Override
    public List<Creatures> findByAppearance(int month, String hemisphere) {
        if (!"北半球".equals(hemisphere) && !"南半球".equals(hemisphere)) {
            throw new IllegalArgumentException("半球は必ず「北半球」か「南半球」を指定してください");
        }

        List<Creatures> allCreatures = atsumoriMapper.selectAll();
        List<Creatures> result = new ArrayList<>();

        for (Creatures creature : allCreatures) {
            List<AppearancePeriod> merged = mergePeriods(creature.getAppearancePeriods(), hemisphere);

            boolean appearsInMonth = merged.stream().anyMatch(p -> p.includesMonth(month));
            if (appearsInMonth) {
                creature.setNorthernAppearancePeriods(mergePeriods(creature.getAppearancePeriods(), "北半球"));
                creature.setSouthernAppearancePeriods(mergePeriods(creature.getAppearancePeriods(), "南半球"));
                result.add(creature);
            }
        }

        return result;
    }

    /**
     * 出現期間をマージするメソッド
     * - 年跨ぎは1つにまとめる
     * - 通常期間は重複している場合だけ統合
     */
    private List<AppearancePeriod> mergePeriods(List<AppearancePeriod> periods, String hemisphere) {
        if (periods == null) return List.of();

        List<AppearancePeriod> filtered = periods.stream()
                .filter(p -> hemisphere.equals(p.getHemisphere()))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) return List.of();

        List<AppearancePeriod> result = new ArrayList<>();

        // 年跨ぎ期間をまとめる
        List<AppearancePeriod> yearCrossing = filtered.stream()
                .filter(p -> p.getStartMonth() > p.getEndMonth())
                .toList();

        if (!yearCrossing.isEmpty()) {
            int start = yearCrossing.stream().mapToInt(AppearancePeriod::getStartMonth).min().getAsInt();
            int end = yearCrossing.stream().mapToInt(AppearancePeriod::getEndMonth).max().getAsInt();
            result.add(new AppearancePeriod(0, hemisphere, start, end));
        }

        // 通常期間（年跨ぎでない）
        List<AppearancePeriod> normal = filtered.stream()
                .filter(p -> p.getStartMonth() <= p.getEndMonth())
                .sorted(Comparator.comparing(AppearancePeriod::getStartMonth))
                .toList();

        int i = 0;
        while (i < normal.size()) {
            int start = normal.get(i).getStartMonth();
            int end = normal.get(i).getEndMonth();

            int j = i + 1;
            while (j < normal.size() && normal.get(j).getStartMonth() <= end) {
                end = Math.max(end, normal.get(j).getEndMonth());
                j++;
            }
            result.add(new AppearancePeriod(0, hemisphere, start, end));
            i = j;
        }

        return result;
    }
}
