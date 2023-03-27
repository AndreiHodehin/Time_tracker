package com.example.trecking_time.entity;

import lombok.*;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultingInfo {
    private String taskName;
    private Duration totalDuration;
    private String success;
}
