package com.example.atsumori.service;

import java.util.List;

import com.example.atsumori.entity.Creatures;

/**
 * Atsumori:サービス
 */
public interface AtsumoriService {
    /**
     * 全生き物情報を検索します
     */
    List<Creatures> findAllCreatures();

    /**
     * 指定されたnameの生き物情報を検索します
     */
    Creatures findByNameCreature(String name);

    /**
     * 指定された月と半球に出現する生き物情報を検索します
     * 
     * @param month 出現月（1〜12）
     * @param hemisphere 半球（"北半球" または "南半球"）
     * @return 該当する生き物のリスト
     */
    List<Creatures> findByAppearance(int month, String hemisphere);

}
