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

        // --- 月ごとの出現フラグ ---
        boolean[] months = new boolean[12];
        for (AppearancePeriod p : targetPeriods) {
            int start = p.getStartMonth();
            int end = p.getEndMonth();
            if (start <= end) {
                for (int m = start; m <= end; m++) months[m - 1] = true;
            } else { // 年をまたぐ
                for (int m = start; m <= 12; m++) months[m - 1] = true;
                for (int m = 1; m <= end; m++) months[m - 1] = true;
            }
        }

        // --- 出現が年をまたいで連続しているか確認 ---
        // 例：11月～3月 のように、12月をまたいで繋がっているか？
        boolean crossesYear = months[11] && months[0];

        // --- 連続区間を抽出 ---
        StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean first = true;

        while (i < 12) {
            if (months[i]) {
                int startMonth = i + 1;
                int j = i;
                while (j < 12 && months[j]) j++;
                int endMonth = j;

                // 年またぎ統合対応
                if (crossesYear && startMonth == 1) {
                    // 1月スタートで年またぎの場合、スキップ（後で11月から出力）
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

        // --- もし全体が年またぎ連続なら 11月～3月 のように統一 ---
        if (crossesYear) {
            // 最初の false の月（途切れた箇所）を探す
            int start = 0, end = 11;
            while (start < 12 && months[start]) start++;
            while (end >= 0 && months[end]) end--;
            return (end + 2) + "月～" + (start) + "月"; // 11～3 のように出力
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

