package com.example.atsumori.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.CaughtFish;

@Mapper
public interface CaughtMapper {
    List<CaughtFish> findByUserId(@Param("userId") Long userId);
    boolean existsByUserIdAndCreatureId(@Param("userId") Long userId, @Param("creatureId") Long creatureId);
    void insertCaughtFish(CaughtFish caughtFish);
}

