package com.greenpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.Goal.Period;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GoalRequestDTO {

    @NotBlank
    private String title;
    private Category category;
    @NotNull @DecimalMin("0.1")
    private BigDecimal targetCo2eKg;
    @NotNull
    private Period period;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    public String getTitle() { return title; }
    public Category getCategory() { return category; }
    public BigDecimal getTargetCo2eKg() { return targetCo2eKg; }
    public Period getPeriod() { return period; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void setTitle(String title) { this.title = title; }
    public void setCategory(Category category) { this.category = category; }
    public void setTargetCo2eKg(BigDecimal targetCo2eKg) { this.targetCo2eKg = targetCo2eKg; }
    public void setPeriod(Period period) { this.period = period; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}