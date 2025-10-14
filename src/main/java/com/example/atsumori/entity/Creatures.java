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

    /** 生き物の名前 */
    private String name;

    /** 価格（ベル） */
    private Integer price;

    /** 生き物の種別（fish, insect, seafoodなど） */
    private String type;

    /** 出現場所リスト */
    private List<Area> areas;

    /** 出現期間リスト（全体） */
    private List<AppearancePeriod> appearancePeriods;

    /** 北半球の出現期間 */
    private List<AppearancePeriod> northernAppearancePeriods;

    /** 南半球の出現期間 */
    private List<AppearancePeriod> southernAppearancePeriods;

    /** 出現時間リスト */
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

        // 月ごとの出現フラグ
        boolean[] months = new boolean[12];
        for (AppearancePeriod p : targetPeriods) {
            int start = p.getStartMonth();
            int end = p.getEndMonth();
            if (start <= end) {
                for (int m = start; m <= end; m++) months[m - 1] = true;
            } else { // 年をまたぐ場合
                for (int m = start; m <= 12; m++) months[m - 1] = true;
                for (int m = 1; m <= end; m++) months[m - 1] = true;
            }
        }

        // 年をまたいで連続しているか
        boolean crossesYear = months[11] && months[0];

        // 連続する出現月を抽出
        StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean first = true;

        while (i < 12) {
            if (months[i]) {
                int startMonth = i + 1;
                int j = i;
                while (j < 12 && months[j]) j++;
                int endMonth = j;

                if (crossesYear && startMonth == 1) {
                    i = j;
                    continue;
                }

                if (!first) sb.append("、");
                if (crossesYear && endMonth == 12) {
                    sb.append(startMonth).append("月～翌").append(3).append("月");
                    break;
                } else {
                    sb.append(startMonth).append("月～").append(endMonth).append("月");
                }
                first = false;
                i = j;
            } else {
                i++;
            }
        }

        if (crossesYear) {
            int start = 0, end = 11;
            while (start < 12 && months[start]) start++;
            while (end >= 0 && months[end]) end--;
            return (end + 2) + "月～" + (start) + "月";
        }

        return sb.toString();
    }

    /** 北半球用の整形済み出現期間を取得 */
    public String getFormattedNorthernPeriod() {
        return getFormattedAppearancePeriod("北半球");
    }

    /** 南半球用の整形済み出現期間を取得 */
    public String getFormattedSouthernPeriod() {
        return getFormattedAppearancePeriod("南半球");
    }
}
