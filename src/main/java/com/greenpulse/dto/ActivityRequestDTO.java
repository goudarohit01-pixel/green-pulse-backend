package com.greenpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.greenpulse.model.Activity.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ActivityRequestDTO {
    @NotNull(message = "Category is required")
    private Category category;
    @NotBlank(message = "Activity type is required")
    private String subType;
    @NotNull @DecimalMin("0.01")
    private BigDecimal quantity;
    private LocalDate activityDate;
    private String notes;

    public Category getCategory() { return category; }
    public String getSubType() { return subType; }
    public BigDecimal getQuantity() { return quantity; }
    public LocalDate getActivityDate() { return activityDate; }
    public String getNotes() { return notes; }
    public void setCategory(Category category) { this.category = category; }
    public void setSubType(String subType) { this.subType = subType; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public void setNotes(String notes) { this.notes = notes; }
}