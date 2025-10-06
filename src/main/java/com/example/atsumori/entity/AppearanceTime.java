package com.example.atsumori.entity;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AppearanceTime {
	private int id;               // 出現時間帯ID
    private LocalTime startTime;  // 出現開始時刻
    private LocalTime endTime;    // 出現終了時刻

}
