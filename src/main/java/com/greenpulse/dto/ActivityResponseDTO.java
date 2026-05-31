package com.greenpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.greenpulse.model.Activity.Category;

public class ActivityResponseDTO {

    private String    id;
    private Category  category;
    private String    subType;
    private BigDecimal quantity;
    private String    unit;
    private BigDecimal co2eKg;
    private LocalDate activityDate;
    private String    notes;
    private LocalDateTime createdAt;

    public ActivityResponseDTO() {}

    public String getId()               { return id;           }
    public Category getCategory()       { return category;     }
    public String getSubType()          { return subType;      }
    public BigDecimal getQuantity()     { return quantity;     }
    public String getUnit()             { return unit;         }
    public BigDecimal getCo2eKg()       { return co2eKg;       }
    public LocalDate getActivityDate()  { return activityDate; }
    public String getNotes()            { return notes;        }
    public LocalDateTime getCreatedAt() { return createdAt;    }

    public void setId(String id)                        { this.id           = id;           }
    public void setCategory(Category category)          { this.category     = category;     }
    public void setSubType(String subType)              { this.subType      = subType;      }
    public void setQuantity(BigDecimal quantity)        { this.quantity     = quantity;     }
    public void setUnit(String unit)                    { this.unit         = unit;         }
    public void setCo2eKg(BigDecimal co2eKg)            { this.co2eKg       = co2eKg;       }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public void setNotes(String notes)                  { this.notes        = notes;        }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt    = createdAt;    }
}