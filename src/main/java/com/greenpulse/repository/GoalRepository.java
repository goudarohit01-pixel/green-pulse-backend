package com.greenpulse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenpulse.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, String> {
    List<Goal> findByUserIdAndActiveTrue(String userId);
    List<Goal> findByUserId(String userId);
}