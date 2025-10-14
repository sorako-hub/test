package com.example.atsumori.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.CaughtCreature;

@Mapper
public interface CaughtMapper {

    // ユーザーが捕った生物ID一覧（タイプ別）
    List<Long> findCreatureIdsByUserIdAndType(@Param("userId") Long userId,
                                              @Param("type") String type);

    // すでに登録済みかチェック（ユーザーID＋生物ID＋タイプ）
    boolean existsByUserIdAndCreatureIdAndType(@Param("userId") Long userId,
                                               @Param("creatureId") Long creatureId,
                                               @Param("type") String type);

    // 捕った生物を登録（CaughtCreatureを受け取り、自動生成されたIDを取得する）
 // ✅ これに統一する（オブジェクト渡し）
    void insertCaughtCreature(CaughtCreature caughtCreature);

}
