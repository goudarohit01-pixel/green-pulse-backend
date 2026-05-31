package com.greenpulse.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.greenpulse.dto.ActivityResponseDTO;
import com.greenpulse.dto.DashboardResponseDTO;
import com.greenpulse.dto.DashboardResponseDTO.DailyPoint;
import com.greenpulse.model.Activity;
import com.greenpulse.model.User;
import com.greenpulse.repository.ActivityRepository;
import com.greenpulse.repository.UserRepository;

@Service
public class DashboardService {

    private final ActivityRepository activityRepository;
    private final UserRepository     userRepository;
    private final ActivityService    activityService;

    private static final BigDecimal NATIONAL_AVG_KG = new BigDecimal("23.00");

    public DashboardService(ActivityRepository activityRepository,
                            UserRepository userRepository,
                            ActivityService activityService) {
        this.activityRepository = activityRepository;
        this.userRepository     = userRepository;
        this.activityService    = activityService;
    }

    public DashboardResponseDTO getSummary(String email) {
        User user = getUser(email);

        // userId is now String
        String userId = user.getId();

        LocalDate today      = LocalDate.now();
        LocalDate weekStart  = today.minusDays(6);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate allTime    = LocalDate.of(2000, 1, 1);

        // ── Totals ────────────────────────────────────────────
        BigDecimal todayCo2   = activityRepository.sumCo2ByUserAndDateRange(userId, today, today);
        BigDecimal weekCo2    = activityRepository.sumCo2ByUserAndDateRange(userId, weekStart, today);
        BigDecimal monthCo2   = activityRepository.sumCo2ByUserAndDateRange(userId, monthStart, today);
        BigDecimal allTimeCo2 = activityRepository.sumCo2ByUserAndDateRange(userId, allTime, today);

        if (todayCo2   == null) todayCo2   = BigDecimal.ZERO;
        if (weekCo2    == null) weekCo2    = BigDecimal.ZERO;
        if (monthCo2   == null) monthCo2   = BigDecimal.ZERO;
        if (allTimeCo2 == null) allTimeCo2 = BigDecimal.ZERO;

        // ── Savings vs national average ───────────────────────
        double savingsPct = 0;
        if (todayCo2.compareTo(BigDecimal.ZERO) > 0) {
            savingsPct = NATIONAL_AVG_KG.subtract(todayCo2)
                .divide(NATIONAL_AVG_KG, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        }

        // ── Category breakdown ────────────────────────────────
        List<Object[]> catRows = activityRepository
            .sumCo2ByCategoryAndDateRange(userId, monthStart, today);

        Map<String, BigDecimal> catMap = new LinkedHashMap<>();
        for (Object[] row : catRows) {
            catMap.put(((Activity.Category) row[0]).name(), (BigDecimal) row[1]);
        }

        // ── Daily trend ───────────────────────────────────────
        List<Object[]> trendRows = activityRepository
            .dailyCo2Trend(userId, weekStart, today);

        List<DailyPoint> trend = new ArrayList<>();
        for (Object[] row : trendRows) {
            trend.add(new DailyPoint(row[0].toString(), (BigDecimal) row[1]));
        }

        // ── Recent 5 activities ───────────────────────────────
        List<ActivityResponseDTO> recent = new ArrayList<>();
        List<Activity> recentRaw = activityRepository
            .findByUserIdAndActivityDateBetweenOrderByActivityDateDesc(
                userId, weekStart, today);

        int count = 0;
        for (Activity a : recentRaw) {
            if (count >= 5) break;
            recent.add(activityService.toDTO(a));
            count++;
        }

        // ── Streak ────────────────────────────────────────────
        long streak = calculateStreak(userId, today);

        // ── Build response ────────────────────────────────────
        DashboardResponseDTO response = new DashboardResponseDTO();
        response.setTodayCo2eKg(todayCo2);
        response.setWeekCo2eKg(weekCo2);
        response.setMonthCo2eKg(monthCo2);
        response.setAllTimeCo2eKg(allTimeCo2);
        response.setSavingsVsNationalPct(savingsPct);
        response.setCurrentStreakDays(streak);
        response.setTotalActivities(activityRepository.countByUserId(userId));
        response.setCategoryBreakdown(catMap);
        response.setWeeklyTrend(trend);
        response.setRecentActivities(recent);
        return response;
    }

    private long calculateStreak(String userId, LocalDate from) {
        long streak = 0;
        LocalDate day = from;
        while (streak <= 365) {
            BigDecimal total = activityRepository
                .sumCo2ByUserAndDateRange(userId, day, day);
            if (total == null || total.compareTo(BigDecimal.ZERO) == 0) break;
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}