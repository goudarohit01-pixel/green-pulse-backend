package com.greenpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.Goal.Period;

public class GoalProgressDTO {

    private String     goalId;
    private String     title;
    private Category   category;
    private Period     period;
    private BigDecimal targetCo2eKg;
    private BigDecimal usedCo2eKg;
    private BigDecimal remainingCo2eKg;
    private double     percentUsed;
    private LocalDate  startDate;
    private LocalDate  endDate;
    private boolean    onTrack;

    public GoalProgressDTO() {}

    public String    getGoalId()          { return goalId;          }
    public String    getTitle()           { return title;           }
    public Category  getCategory()        { return category;        }
    public Period    getPeriod()          { return period;          }
    public BigDecimal getTargetCo2eKg()   { return targetCo2eKg;   }
    public BigDecimal getUsedCo2eKg()     { return usedCo2eKg;     }
    public BigDecimal getRemainingCo2eKg(){ return remainingCo2eKg; }
    public double    getPercentUsed()     { return percentUsed;     }
    public LocalDate getStartDate()       { return startDate;       }
    public LocalDate getEndDate()         { return endDate;         }
    public boolean   isOnTrack()          { return onTrack;         }

    public void setGoalId(String goalId)                    { this.goalId          = goalId;          }
    public void setTitle(String title)                      { this.title           = title;           }
    public void setCategory(Category category)              { this.category        = category;        }
    public void setPeriod(Period period)                    { this.period          = period;          }
    public void setTargetCo2eKg(BigDecimal targetCo2eKg)   { this.targetCo2eKg   = targetCo2eKg;   }
    public void setUsedCo2eKg(BigDecimal usedCo2eKg)       { this.usedCo2eKg     = usedCo2eKg;     }
    public void setRemainingCo2eKg(BigDecimal remaining)   { this.remainingCo2eKg = remaining;       }
    public void setPercentUsed(double percentUsed)         { this.percentUsed     = percentUsed;     }
    public void setStartDate(LocalDate startDate)          { this.startDate       = startDate;       }
    public void setEndDate(LocalDate endDate)              { this.endDate         = endDate;         }
    public void setOnTrack(boolean onTrack)                { this.onTrack         = onTrack;         }
}