package com.greenpulse.service;

import com.greenpulse.dto.ActivityRequestDTO;
import com.greenpulse.dto.ActivityResponseDTO;
import com.greenpulse.model.Activity;
import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.User;
import com.greenpulse.repository.ActivityRepository;
import com.greenpulse.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository      activityRepository;
    private final UserRepository          userRepository;
    private final CarbonCalculatorService calculator;

    public ActivityService(ActivityRepository activityRepository,
                           UserRepository userRepository,
                           CarbonCalculatorService calculator) {
        this.activityRepository = activityRepository;
        this.userRepository     = userRepository;
        this.calculator         = calculator;
    }

    // ── Log Activity ─────────────────────────────────────────
    @Transactional
    public ActivityResponseDTO logActivity(String email, ActivityRequestDTO req) {
        User user   = getUser(email);
        var  co2eKg = calculator.calculate(
            req.getCategory(), req.getSubType(), req.getQuantity());
        var  unit   = calculator.getUnit(
            req.getCategory(), req.getSubType());

        Activity activity = new Activity();
        activity.setUser(user);
        activity.setCategory(req.getCategory());
        activity.setSubType(req.getSubType());
        activity.setQuantity(req.getQuantity());
        activity.setUnit(unit);
        activity.setCo2eKg(co2eKg);
        activity.setActivityDate(
            req.getActivityDate() != null
                ? req.getActivityDate()
                : LocalDate.now());
        activity.setNotes(req.getNotes());

        return toDTO(activityRepository.save(activity));
    }

    // ── Get Activities ───────────────────────────────────────
    public List<ActivityResponseDTO> getActivities(
            String email, LocalDate from, LocalDate to, Category category) {

        // userId is now String
        String userId = getUser(email).getId();
        List<Activity> activities;

        if (from != null && to != null && category != null) {
            activities = activityRepository
                .findByUserIdAndActivityDateBetweenOrderByActivityDateDesc(
                    userId, from, to);
            activities.removeIf(a -> a.getCategory() != category);
        } else if (from != null && to != null) {
            activities = activityRepository
                .findByUserIdAndActivityDateBetweenOrderByActivityDateDesc(
                    userId, from, to);
        } else if (category != null) {
            activities = activityRepository
                .findByUserIdAndCategoryOrderByActivityDateDesc(
                    userId, category);
        } else {
            activities = activityRepository
                .findByUserIdOrderByActivityDateDesc(userId);
        }

        List<ActivityResponseDTO> result = new ArrayList<>();
        for (Activity a : activities) {
            result.add(toDTO(a));
        }
        return result;
    }

    // ── Delete Activity ──────────────────────────────────────
    @Transactional
    public void deleteActivity(String email, String activityId) {
        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() ->
                new IllegalArgumentException("Activity not found"));

        if (!activity.getUser().getEmail().equals(email)) {
            throw new SecurityException("Access denied");
        }
        activityRepository.delete(activity);
    }

    // ── toDTO ────────────────────────────────────────────────
    public ActivityResponseDTO toDTO(Activity a) {
        ActivityResponseDTO dto = new ActivityResponseDTO();
        dto.setId(a.getId());
        dto.setCategory(a.getCategory());
        dto.setSubType(a.getSubType());
        dto.setQuantity(a.getQuantity());
        dto.setUnit(a.getUnit());
        dto.setCo2eKg(a.getCo2eKg());
        dto.setActivityDate(a.getActivityDate());
        dto.setNotes(a.getNotes());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + email));
    }
}