package com.example.trecking_time.entity.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Activity {

    private Long id;
    private String name;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double expectationDuration;
    private boolean inAction;
    private Long userId;
}
