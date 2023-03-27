package com.example.trecking_time.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class ActivityEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double expectationDuration;
    private boolean inAction;
}
