package com.example.atsumori.service;

import java.util.List;

import com.example.atsumori.entity.Creatures;

public interface AtsumoriService {

    List<Creatures> findAllCreatures();

    Creatures findByNameCreature(String name);

    List<Creatures> findByAppearance(int month, String hemisphere);

    // ユーザーIDを追加して釣った魚を取得
    List<Creatures> findCaughtFishByUserId(Long userId);

    // ユーザーIDを追加してまだ釣っていない魚を取得
    List<Creatures> findUncaughtFishByUserId(Long userId);

    // ユーザーIDを追加して、指定月・半球で釣った魚を取得
    List<Creatures> findCaughtFishByAppearance(Long userId, int month, String hemisphere);

    // ユーザーIDを追加して、指定月・半球でまだ釣っていない魚を取得
    List<Creatures> findUncaughtFishByAppearance(Long userId, int month, String hemisphere);
}
