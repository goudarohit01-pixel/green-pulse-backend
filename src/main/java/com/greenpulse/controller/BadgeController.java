package com.greenpulse.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenpulse.service.BadgeService;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // GET /api/badges
    // Returns all badges with earned status for current user
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getBadges(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            badgeService.getBadges(user.getUsername()));
    }
}