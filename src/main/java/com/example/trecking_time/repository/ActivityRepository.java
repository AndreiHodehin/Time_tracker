package com.example.trecking_time.repository;


import com.example.trecking_time.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    List<ActivityEntity> findAllByDay(LocalDate date);
    @Query("select a from ActivityEntity a join fetch a.user where a.day=?1 and a.user.id=?2")
    List<ActivityEntity> findAllByDayAndUser(LocalDate date,Long id);
}
