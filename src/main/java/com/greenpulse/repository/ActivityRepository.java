package com.greenpulse.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.greenpulse.model.Activity;
import com.greenpulse.model.Activity.Category;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    List<Activity> findByUserIdOrderByActivityDateDesc(String userId);

    List<Activity> findByUserIdAndActivityDateBetweenOrderByActivityDateDesc(
        String userId, LocalDate from, LocalDate to);

    List<Activity> findByUserIdAndCategoryOrderByActivityDateDesc(
        String userId, Category category);

    @Query("SELECT COALESCE(SUM(a.co2eKg), 0) FROM Activity a " +
           "WHERE a.user.id = :userId " +
           "AND a.activityDate BETWEEN :from AND :to")
    BigDecimal sumCo2ByUserAndDateRange(
        @Param("userId") String userId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    @Query("SELECT a.category, COALESCE(SUM(a.co2eKg), 0) FROM Activity a " +
           "WHERE a.user.id = :userId " +
           "AND a.activityDate BETWEEN :from AND :to " +
           "GROUP BY a.category")
    List<Object[]> sumCo2ByCategoryAndDateRange(
        @Param("userId") String userId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    @Query("SELECT a.activityDate, COALESCE(SUM(a.co2eKg), 0) FROM Activity a " +
           "WHERE a.user.id = :userId " +
           "AND a.activityDate BETWEEN :from AND :to " +
           "GROUP BY a.activityDate ORDER BY a.activityDate ASC")
    List<Object[]> dailyCo2Trend(
        @Param("userId") String userId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    long countByUserId(String userId);
}