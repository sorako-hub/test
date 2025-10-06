package com.example.atsumori.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生き物エンティティ
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creatures {
    /** 生き物ID */
    private Integer id;
    /** 生き物名前 */
    private String name;
    /** 価格 */
    private Integer price;

    /** 出現場所 */
    private List<Area> areas;

    /** 出現期間 */
    private List<AppearancePeriod> appearancePeriods;
    private List<AppearancePeriod> northernAppearancePeriods; 
    private List<AppearancePeriod> southernAppearancePeriods;

    /** 出現時間 */
    private List<AppearanceTime> appearanceTimes;
}
