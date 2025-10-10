package com.example.atsumori.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.Creatures;

@Mapper
public interface AtsumoriMapper {

    List<Creatures> selectAll();

    Creatures selectByName(String name);

    List<Creatures> selectByAppearance(@Param("month") int month, @Param("hemisphere") String hemisphere);

    Creatures selectById(Long id);

    List<Creatures> findByIdNotIn(List<Long> ids);

    /**
     * ユーザーIDで釣った魚一覧を取得
     */
    List<Creatures> selectCaughtFish(@Param("userId") Long userId);

    /**
     * ユーザーIDでまだ釣っていない魚一覧を取得
     */
    List<Creatures> selectUncaughtFish(@Param("userId") Long userId);

    /**
     * ユーザーIDで、指定された月・半球に出現し釣った魚一覧を取得
     */
    List<Creatures> selectCaughtFishByAppearance(@Param("userId") Long userId,
                                                @Param("month") int month,
                                                @Param("hemisphere") String hemisphere);

    /**
     * ユーザーIDで、指定された月・半球に出現しまだ釣っていない魚一覧を取得
     */
    List<Creatures> selectUncaughtFishByAppearance(@Param("userId") Long userId,
                                                  @Param("month") int month,
                                                  @Param("hemisphere") String hemisphere);
}
