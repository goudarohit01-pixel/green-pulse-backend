package com.greenpulse.service;

import com.greenpulse.dto.GoalProgressDTO;
import com.greenpulse.dto.GoalRequestDTO;
import com.greenpulse.model.Goal;
import com.greenpulse.model.User;
import com.greenpulse.repository.ActivityRepository;
import com.greenpulse.repository.GoalRepository;
import com.greenpulse.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository     goalRepository;
    private final UserRepository     userRepository;
    private final ActivityRepository activityRepository;

    public GoalService(GoalRepository goalRepository,
                       UserRepository userRepository,
                       ActivityRepository activityRepository) {
        this.goalRepository     = goalRepository;
        this.userRepository     = userRepository;
        this.activityRepository = activityRepository;
    }

    // ── Create Goal ──────────────────────────────────────────
    @Transactional
    public GoalProgressDTO createGoal(String email, GoalRequestDTO req) {
        User user = getUser(email);

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(req.getTitle());
        goal.setCategory(req.getCategory());
        goal.setTargetCo2eKg(req.getTargetCo2eKg());
        goal.setPeriod(req.getPeriod());
        goal.setStartDate(req.getStartDate());
        goal.setEndDate(req.getEndDate());
        goal.setActive(true);

        return toProgress(goalRepository.save(goal), user.getId());
    }

    // ── Get Goals with Progress ──────────────────────────────
    public List<GoalProgressDTO> getGoalsWithProgress(String email) {
        User user = getUser(email);
        List<Goal> goals = goalRepository.findByUserIdAndActiveTrue(user.getId());

        List<GoalProgressDTO> result = new ArrayList<>();
        for (Goal goal : goals) {
            result.add(toProgress(goal, user.getId()));
        }
        return result;
    }

    // ── Delete Goal ──────────────────────────────────────────
    @Transactional
    public void deleteGoal(String email, String goalId) {
        Goal goal = goalRepository.findById(goalId)
            .orElseThrow(() ->
                new IllegalArgumentException("Goal not found"));

        if (!goal.getUser().getEmail().equals(email)) {
            throw new SecurityException("Access denied");
        }
        goalRepository.delete(goal);
    }

    // ── Convert to DTO ───────────────────────────────────────
    private GoalProgressDTO toProgress(Goal goal, String userId) {
        BigDecimal used = activityRepository.sumCo2ByUserAndDateRange(
            userId, goal.getStartDate(), goal.getEndDate());

        if (used == null) used = BigDecimal.ZERO;

        BigDecimal remaining = goal.getTargetCo2eKg()
            .subtract(used).max(BigDecimal.ZERO);

        double pct = 0;
        if (goal.getTargetCo2eKg().compareTo(BigDecimal.ZERO) > 0) {
            pct = used
                .divide(goal.getTargetCo2eKg(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        }

        GoalProgressDTO dto = new GoalProgressDTO();
        dto.setGoalId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setCategory(goal.getCategory());
        dto.setPeriod(goal.getPeriod());
        dto.setTargetCo2eKg(goal.getTargetCo2eKg());
        dto.setUsedCo2eKg(used);
        dto.setRemainingCo2eKg(remaining);
        dto.setPercentUsed(pct);
        dto.setStartDate(goal.getStartDate());
        dto.setEndDate(goal.getEndDate());
        dto.setOnTrack(pct < 85);
        return dto;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}