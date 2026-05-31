package com.greenpulse.service;

import com.greenpulse.model.Activity;
import com.greenpulse.model.User;
import com.greenpulse.repository.ActivityRepository;
import com.greenpulse.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class BadgeService {

    private final ActivityRepository activityRepository;
    private final UserRepository     userRepository;

    public BadgeService(ActivityRepository activityRepository,
                        UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository     = userRepository;
    }

    /**
     * Calculate which badges a user has earned
     * based on their real activity data
     */
    public List<Map<String, Object>> getBadges(String email) {
        User   user   = getUser(email);
        String userId = user.getId();

        LocalDate today      = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart  = today.withDayOfYear(1);

        // Get all activities
        List<Activity> allActivities =
            activityRepository.findByUserIdOrderByActivityDateDesc(userId);

        // ── Badge 1: First Log ───────────────────────────────
        // Earned when user has logged at least 1 activity
        boolean firstLog = !allActivities.isEmpty();

        // ── Badge 2: Bike Week ───────────────────────────────
        // Earned when user has no TRANSPORT activities for 7 consecutive days
        boolean bikeWeek = checkBikeWeek(allActivities, today);

        // ── Badge 3: Plant Powered ───────────────────────────
        // Earned when user has logged only veg/vegan food
        // for at least 30 days (no beef, lamb, pork, chicken, fish)
        boolean plantPowered = checkPlantPowered(allActivities);

        // ── Badge 4: Energy Saver ────────────────────────────
        // Earned when monthly energy CO2 is under 50 kg
        // (UK average household monthly energy ~70 kg CO2)
        BigDecimal energyCo2ThisMonth = BigDecimal.ZERO;
        for (Activity a : allActivities) {
            if (a.getCategory() == Activity.Category.ENERGY
                    && !a.getActivityDate().isBefore(monthStart)) {
                energyCo2ThisMonth = energyCo2ThisMonth.add(a.getCo2eKg());
            }
        }
        boolean energySaver = energyCo2ThisMonth.compareTo(new BigDecimal("50")) < 0
                              && !allActivities.isEmpty();

        // ── Badge 5: Carbon Neutral ──────────────────────────
        // Earned when total CO2 for this month is under 10 kg
        BigDecimal totalThisMonth = activityRepository
            .sumCo2ByUserAndDateRange(userId, monthStart, today);
        if (totalThisMonth == null) totalThisMonth = BigDecimal.ZERO;
        boolean carbonNeutral = totalThisMonth.compareTo(new BigDecimal("10")) < 0
                                && !allActivities.isEmpty();

        // ── Badge 6: Streak Master ───────────────────────────
        // Earned when user has logged activities for 7 days in a row
        boolean streakMaster = checkStreak(userId, today, 7);

        // ── Build badge list ─────────────────────────────────
        List<Map<String, Object>> badges = new ArrayList<>();

        badges.add(buildBadge(
            "🌱", "First Log",
            "Logged your first activity",
            firstLog,
            firstLog ? "Earned by logging your first activity" : "Log your first activity to earn this"
        ));

        badges.add(buildBadge(
            "🚴", "Bike Week",
            "7 days with zero transport emissions",
            bikeWeek,
            bikeWeek ? "You went 7 days without transport emissions!" : "Avoid transport for 7 consecutive days"
        ));

        badges.add(buildBadge(
            "🥗", "Plant Powered",
            "Logged only plant-based food",
            plantPowered,
            plantPowered ? "You only logged plant-based food!" : "Log only plant-based food (no meat or fish)"
        ));

        badges.add(buildBadge(
            "⚡", "Energy Saver",
            "Energy CO2 under 50 kg this month",
            energySaver,
            energySaver
                ? "Your energy use is below 50 kg CO2e this month!"
                : String.format("Current: %.1f kg / target: 50 kg", energyCo2ThisMonth)
        ));

        badges.add(buildBadge(
            "🌍", "Carbon Neutral",
            "Total CO2 under 10 kg this month",
            carbonNeutral,
            carbonNeutral
                ? "Amazing! You are nearly carbon neutral this month!"
                : String.format("Current: %.1f kg / target: under 10 kg", totalThisMonth)
        ));

        badges.add(buildBadge(
            "🏆", "Streak Master",
            "Logged activities 7 days in a row",
            streakMaster,
            streakMaster ? "You logged activities for 7 days in a row!" : "Log activities for 7 consecutive days"
        ));

        return badges;
    }

    // ── Helpers ──────────────────────────────────────────────

    private boolean checkBikeWeek(List<Activity> activities, LocalDate today) {
        // Check last 7 days for zero transport activities
        LocalDate sevenDaysAgo = today.minusDays(6);
        Set<LocalDate> transportDays = new HashSet<>();

        for (Activity a : activities) {
            if (a.getCategory() == Activity.Category.TRANSPORT
                    && !a.getActivityDate().isBefore(sevenDaysAgo)
                    && !a.getActivityDate().isAfter(today)) {
                transportDays.add(a.getActivityDate());
            }
        }
        // User has no transport in last 7 days AND has logged something
        return transportDays.isEmpty() && !activities.isEmpty();
    }

    private boolean checkPlantPowered(List<Activity> activities) {
        // Check if user has ANY food activities
        // and NONE of them are meat
        Set<String> meatTypes = new HashSet<>(Arrays.asList(
            "beef", "lamb", "pork", "chicken", "fish"
        ));

        boolean hasFoodActivities = false;
        boolean hasMeat           = false;

        for (Activity a : activities) {
            if (a.getCategory() == Activity.Category.FOOD) {
                hasFoodActivities = true;
                if (meatTypes.contains(a.getSubType().toLowerCase())) {
                    hasMeat = true;
                    break;
                }
            }
        }

        return hasFoodActivities && !hasMeat;
    }

    private boolean checkStreak(String userId, LocalDate from, int days) {
        int streak = 0;
        LocalDate day = from;

        while (streak < days) {
            BigDecimal total = activityRepository
                .sumCo2ByUserAndDateRange(userId, day, day);
            if (total == null || total.compareTo(BigDecimal.ZERO) == 0) break;
            streak++;
            day = day.minusDays(1);
        }

        return streak >= days;
    }

    private Map<String, Object> buildBadge(
            String icon, String label,
            String desc, boolean earned, String progress) {

        Map<String, Object> badge = new LinkedHashMap<>();
        badge.put("icon",     icon);
        badge.put("label",    label);
        badge.put("desc",     desc);
        badge.put("earned",   earned);
        badge.put("progress", progress);
        return badge;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}