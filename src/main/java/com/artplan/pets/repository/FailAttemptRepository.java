package com.artplan.pets.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artplan.pets.entity.FailAttempt;

public interface FailAttemptRepository extends JpaRepository<FailAttempt, Long> {
    void deleteByUsername(String username);
    Long countByUsernameAndAttemptTimeGreaterThan(String username, LocalDateTime attemptTime);
}
