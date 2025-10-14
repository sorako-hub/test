package com.example.atsumori.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.atsumori.entity.CaughtCreature;
import com.example.atsumori.entity.Creatures;
import com.example.atsumori.entity.LoginUser;
import com.example.atsumori.repository.AtsumoriMapper;
import com.example.atsumori.repository.CaughtMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatchService {

    private final AtsumoriMapper atsumoriMapper;
    private final CaughtMapper caughtMapper;

    /**
     * ログインユーザーがまだ捕っていない（釣っていない）生物をタイプ別で取得
     */
    public List<Creatures> getUncaughtCreaturesByType(LoginUser user, String type) {
        List<Long> caughtCreatureIds = caughtMapper.findCreatureIdsByUserIdAndType(user.getId(), type);
        System.out.println("取得した捕った" + type + "のIDリスト: " + caughtCreatureIds);

        if (caughtCreatureIds.isEmpty()) {
            return atsumoriMapper.selectByType(type);
        } else {
            return atsumoriMapper.selectUncaughtCreaturesByType(user.getId(), type);
        }
    }

    /**
     * 複数の生物をまとめて登録する処理（釣った魚、捕った虫、獲った海の幸など）
     */
    @Transactional
    public void registerMultipleCreatures(LoginUser user, List<Long> creatureIds, String type) {
        for (Long creatureId : creatureIds) {
            Creatures creature = atsumoriMapper.selectById(creatureId);

            if (creature == null || !creature.getType().equals(type)) {
                System.out.println("無効な" + type + "ID: " + creatureId);
                continue;
            }

            boolean alreadyCaught = caughtMapper.existsByUserIdAndCreatureIdAndType(user.getId(), creatureId, type);
            if (alreadyCaught) {
                System.out.println("すでに登録済みの" + type + "ID: " + creatureId);
                continue;
            }

            // CaughtCreatureオブジェクトを作成
            CaughtCreature caughtCreature = new CaughtCreature();
            caughtCreature.setUserId(user.getId());
            caughtCreature.setCreatureId(creatureId);
            caughtCreature.setType(type);

            // 登録
            caughtMapper.insertCaughtCreature(caughtCreature);

            Long generatedId = caughtCreature.getId(); // 自動生成ID
            System.out.println("登録完了したID: " + generatedId);
        }
    }
}
