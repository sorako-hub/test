package com.example.atsumori.entity;

import lombok.Data;

@Data
public class CaughtFish {
    private Long id;
    private Long userId;     // UserのIDを直接持つ
    private Long creatureId; // CreaturesのIDを直接持つ

    // getter/setter省略
}
