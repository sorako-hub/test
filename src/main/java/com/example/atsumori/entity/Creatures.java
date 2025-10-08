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

    /**
     * 出現期間を「11月〜3月」のように整形して返す
     * 
     * @param hemisphere 北半球 or 南半球
     * @return 整形された出現期間文字列
     */
    public String getFormattedAppearancePeriod(String hemisphere) {
        List<AppearancePeriod> targetPeriods;

        if ("北半球".equals(hemisphere)) {
            targetPeriods = this.northernAppearancePeriods;
        } else if ("南半球".equals(hemisphere)) {
            targetPeriods = this.southernAppearancePeriods;
        } else {
            return "";
        }

        if (targetPeriods == null || targetPeriods.isEmpty()) {
            return "なし";
        }
        
        // 月の出現フラグを作成（0:1月〜11:12月）
        boolean[] months = new boolean[12];
        for (AppearancePeriod p : targetPeriods) {
            int start = p.getStartMonth();
            int end = p.getEndMonth();
            if (start <= end) {
                for (int m = start; m <= end; m++) {
                    months[m - 1] = true;
                }
            } else { // 年をまたぐ場合
                for (int m = start; m <= 12; m++) months[m - 1] = true;
                for (int m = 1; m <= end; m++) months[m - 1] = true;
            }
        }

        // 連続する期間を抽出
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 12) {
            if (months[i]) {
                int startMonth = i + 1;
                int j = i;
                while (months[j % 12]) {
                    j++;
                    if (j - i >= 12) break; // 無限ループ防止
                }
                int endMonth = (j % 12 == 0) ? 12 : j % 12;
                boolean crossesYear = startMonth > endMonth;
                if (sb.length() > 0) sb.append("、");
                if (crossesYear) {
                    sb.append(startMonth).append("月〜翌").append(endMonth).append("月");
                } else {
                    sb.append(startMonth).append("月〜").append(endMonth).append("月");
                }
                i = j;
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    /** 北半球用整形出現期間 */
    public String getFormattedNorthernPeriod() {
        return getFormattedAppearancePeriod("北半球");
    }

    /** 南半球用整形出現期間 */
    public String getFormattedSouthernPeriod() {
        return getFormattedAppearancePeriod("南半球");
    }
}

