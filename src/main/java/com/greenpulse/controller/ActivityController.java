package com.greenpulse.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenpulse.dto.ActivityRequestDTO;
import com.greenpulse.dto.ActivityResponseDTO;
import com.greenpulse.model.Activity.Category;
import com.greenpulse.service.ActivityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponseDTO> log(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ActivityRequestDTO req) {
        return ResponseEntity.ok(
            activityService.logActivity(user.getUsername(), req));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDTO>> getAll(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Category category) {
        return ResponseEntity.ok(
            activityService.getActivities(user.getUsername(), from, to, category));
    }

    // Changed UUID to String
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String id) {
        activityService.deleteActivity(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}