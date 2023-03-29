package com.example.trecking_time.repository;

import com.example.trecking_time.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    void removeByName(String name);

}
