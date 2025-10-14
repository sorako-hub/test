package com.example.atsumori.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.Creatures;

@Mapper
public interface AtsumoriMapper {

    /** 全生き物を取得 */
    List<Creatures> selectAll();

    /** タイプ別に生き物一覧を取得（fish, insect, seafoodなど） */
    List<Creatures> selectByType(@Param("type") String type);

    /** 名前とタイプで生き物を1件取得 */
    Creatures selectByNameAndType(@Param("name") String name, @Param("type") String type);

    /** 生き物IDで取得（詳細表示など用） */
    Creatures selectById(Long id);

    /** IDリストに含まれない生き物（未取得リスト用など） */
    List<Creatures> findByIdNotIn(List<Long> ids);

    // -------------------------
    // ▼ ユーザー取得関連（タイプ別）
    // -------------------------

    /** 指定ユーザーが取得済みの生き物（タイプ別） */
    List<Creatures> selectCaughtCreaturesByType(@Param("userId") Long userId, @Param("type") String type);

    /** 指定ユーザーが未取得の生き物（タイプ別） */
    List<Creatures> selectUncaughtCreaturesByType(@Param("userId") Long userId, @Param("type") String type);

    // -------------------------
    // ▼ 外部でフィルタして使うため、出現情報付きの全件取得（タイプ付き）を Controller 側で使用
    // -------------------------

    /** 月・半球検索（タイプ付き）はJava側でフィルタリングするので、ここでは selectByType を使うだけでOK */

}

