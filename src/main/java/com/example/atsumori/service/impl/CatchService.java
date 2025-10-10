package com.example.atsumori.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.atsumori.entity.CaughtFish;
import com.example.atsumori.entity.Creatures;
import com.example.atsumori.entity.LoginUser;
import com.example.atsumori.repository.AtsumoriMapper;
import com.example.atsumori.repository.CaughtMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatchService {

    private final AtsumoriMapper AtsumoriMapper;
    private final CaughtMapper caughtMapper;

    /**
     * ログインユーザーが釣っていない魚の一覧を取得
     */
    public List<Creatures> getUncaughtFish(LoginUser user) {
        // ユーザーが釣った魚一覧を取得
        List<CaughtFish> caughtFishList = caughtMapper.findByUserId(user.getId());
        System.out.println("取得した釣った魚のリスト: " + caughtFishList);

        // 釣った魚のcreatureIdだけ抽出
        List<Long> caughtFishIds = caughtFishList.stream()
                                                 .map(CaughtFish::getCreatureId)
                                                 .collect(Collectors.toList());

        System.out.println("caughtFishIds = " + caughtFishIds);

        if (caughtFishIds.isEmpty()) {
            return AtsumoriMapper.selectAll();
        } else {
            return AtsumoriMapper.findByIdNotIn(caughtFishIds);
        }
    }

    /**
     * 複数の魚をまとめて登録する処理
     */
    @Transactional
    public void registerMultipleFish(LoginUser user, List<Long> fishIds) {
        for (Long fishId : fishIds) {
            // 魚が存在するかチェック（念のため）
            Creatures creature = AtsumoriMapper.selectById(fishId);
            if (creature == null) {
                System.out.println("無効な魚ID: " + fishId);
                continue;
            }

            // すでに登録済みかチェック
            boolean alreadyCaught = caughtMapper.existsByUserIdAndCreatureId(user.getId(), fishId);
            if (alreadyCaught) {
                System.out.println("すでに登録済みの魚ID: " + fishId);
                continue;
            }

            // 登録処理
            CaughtFish caughtFish = new CaughtFish();
            caughtFish.setUserId(user.getId());
            caughtFish.setCreatureId(fishId);
            caughtMapper.insertCaughtFish(caughtFish);
        }
    }
}
