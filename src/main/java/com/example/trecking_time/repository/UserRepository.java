package com.example.trecking_time.repository;

import com.example.trecking_time.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}
