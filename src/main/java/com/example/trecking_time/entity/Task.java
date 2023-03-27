package com.example.trecking_time.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Task(String name) {
        this.name = name;
    }
}
