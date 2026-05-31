package com.greenpulse.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponseDTO {

    private BigDecimal todayCo2eKg;
    private BigDecimal weekCo2eKg;
    private BigDecimal monthCo2eKg;
    private BigDecimal allTimeCo2eKg;
    private double savingsVsNationalPct;
    private long currentStreakDays;
    private long totalActivities;
    private Map<String, BigDecimal> categoryBreakdown;
    private List<DailyPoint> weeklyTrend;
    private List<ActivityResponseDTO> recentActivities;

    public DashboardResponseDTO() {}

    // ── Inner class ──────────────────────────────────────────
    public static class DailyPoint {
        private String date;
        private BigDecimal co2eKg;

        public DailyPoint() {}
        public DailyPoint(String date, BigDecimal co2eKg) {
            this.date = date;
            this.co2eKg = co2eKg;
        }

        public String getDate() { return date; }
        public BigDecimal getCo2eKg() { return co2eKg; }
        public void setDate(String date) { this.date = date; }
        public void setCo2eKg(BigDecimal co2eKg) { this.co2eKg = co2eKg; }
    }

    // ── Getters ──────────────────────────────────────────────
    public BigDecimal getTodayCo2eKg() { return todayCo2eKg; }
    public BigDecimal getWeekCo2eKg() { return weekCo2eKg; }
    public BigDecimal getMonthCo2eKg() { return monthCo2eKg; }
    public BigDecimal getAllTimeCo2eKg() { return allTimeCo2eKg; }
    public double getSavingsVsNationalPct() { return savingsVsNationalPct; }
    public long getCurrentStreakDays() { return currentStreakDays; }
    public long getTotalActivities() { return totalActivities; }
    public Map<String, BigDecimal> getCategoryBreakdown() { return categoryBreakdown; }
    public List<DailyPoint> getWeeklyTrend() { return weeklyTrend; }
    public List<ActivityResponseDTO> getRecentActivities() { return recentActivities; }

    // ── Setters ──────────────────────────────────────────────
    public void setTodayCo2eKg(BigDecimal todayCo2eKg) { this.todayCo2eKg = todayCo2eKg; }
    public void setWeekCo2eKg(BigDecimal weekCo2eKg) { this.weekCo2eKg = weekCo2eKg; }
    public void setMonthCo2eKg(BigDecimal monthCo2eKg) { this.monthCo2eKg = monthCo2eKg; }
    public void setAllTimeCo2eKg(BigDecimal allTimeCo2eKg) { this.allTimeCo2eKg = allTimeCo2eKg; }
    public void setSavingsVsNationalPct(double savingsVsNationalPct) { this.savingsVsNationalPct = savingsVsNationalPct; }
    public void setCurrentStreakDays(long currentStreakDays) { this.currentStreakDays = currentStreakDays; }
    public void setTotalActivities(long totalActivities) { this.totalActivities = totalActivities; }
    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
    public void setWeeklyTrend(List<DailyPoint> weeklyTrend) { this.weeklyTrend = weeklyTrend; }
    public void setRecentActivities(List<ActivityResponseDTO> recentActivities) { this.recentActivities = recentActivities; }
}