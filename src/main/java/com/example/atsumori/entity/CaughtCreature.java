package com.example.atsumori.entity;

import lombok.Data;

@Data
public class CaughtCreature {
    private Long id;
    private Long userId;      // UserのID
    private Long creatureId;  // CreaturesのID
    private String type;      // 種類（例: fish, bug, seafood）

    // getter/setterは@Dataで自動生成されるので省略OK
}
