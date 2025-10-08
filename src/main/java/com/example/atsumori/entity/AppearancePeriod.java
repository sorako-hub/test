package com.example.atsumori.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppearancePeriod {
    private int id;                // 出現期間ID
    private String hemisphere;     // 半球（北半球、南半球、NULL）
    private int startMonth;        // 出現開始月
    private int endMonth;          // 出現終了月

    /**
     * 指定した月がこの出現期間に含まれるか判定するメソッド
     * @param month 判定したい月（1〜12）
     * @return 出現期間に含まれていればtrue、それ以外はfalse
     */
    public boolean includesMonth(int month) {
        if (startMonth <= endMonth) {
            // 期間が1月→5月など通常パターン
            return month >= startMonth && month <= endMonth;
        } else {
            // 期間が11月→3月など年跨ぎパターン
            return month >= startMonth || month <= endMonth;
        }
    }
}
