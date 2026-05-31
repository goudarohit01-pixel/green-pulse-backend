package com.greenpulse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenpulse.dto.GoalProgressDTO;
import com.greenpulse.dto.GoalRequestDTO;
import com.greenpulse.service.GoalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<List<GoalProgressDTO>> getAll(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            goalService.getGoalsWithProgress(user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<GoalProgressDTO> create(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody GoalRequestDTO req) {
        return ResponseEntity.ok(
            goalService.createGoal(user.getUsername(), req));
    }

    // Changed UUID to String
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String id) {
        goalService.deleteGoal(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}