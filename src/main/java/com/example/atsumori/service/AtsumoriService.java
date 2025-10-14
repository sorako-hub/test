package com.example.atsumori.service;

import java.util.List;

import com.example.atsumori.entity.Creatures;

public interface AtsumoriService {

    /** 全生き物取得（使用していれば残す） */
    List<Creatures> findAllCreatures();

    /** タイプ別に生き物一覧を取得（fish, insect, seafoodなど） */
    List<Creatures> findCreaturesByType(String type);

    /** 名前とタイプで生き物を1件取得 */
    Creatures findByNameAndType(String name, String type);

    /** 月・半球・タイプで出現する生き物を取得 */
    List<Creatures> findByAppearanceAndType(int month, String hemisphere, String type);

    /** ユーザーが取得済みの生き物をタイプ指定で取得 */
    List<Creatures> findCaughtCreaturesByType(Long userId, String type);

    /** ユーザーが未取得の生き物をタイプ指定で取得 */
    List<Creatures> findUncaughtCreaturesByType(Long userId, String type);

    /** 月・半球・タイプで、ユーザーが取得済みの生き物を取得 */
    List<Creatures> findCaughtCreaturesByAppearance(Long userId, int month, String hemisphere, String type);

    /** 月・半球・タイプで、ユーザーが未取得の生き物を取得 */
    List<Creatures> findUncaughtCreaturesByAppearance(Long userId, int month, String hemisphere, String type);
}
