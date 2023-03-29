package com.example.trecking_time.repository;


import com.example.trecking_time.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    List<ActivityEntity> findAllByDay(LocalDate date);
}
