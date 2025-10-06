package com.example.atsumori.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.Creatures;

/**
 * Atsumori:リポジトリ
 */
@Mapper
public interface AtsumoriMapper {
    List<Creatures> selectAll();

    Creatures selectByName(@Param("name") String name);

    List<Creatures> selectByAppearance(@Param("month") int month, @Param("hemisphere") String hemisphere);
}

