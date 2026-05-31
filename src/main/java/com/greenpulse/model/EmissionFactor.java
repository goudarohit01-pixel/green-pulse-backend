package com.greenpulse.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emission_factors")
public class EmissionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Activity.Category category;

    @Column(name = "sub_type", nullable = false, length = 100)
    private String subType;

    @Column(name = "factor_kg_co2e", nullable = false, precision = 10, scale = 6)
    private BigDecimal factorKgCo2e;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(length = 100)
    private String source;

    public EmissionFactor() {}

    public Long getId() { return id; }
    public Activity.Category getCategory() { return category; }
    public String getSubType() { return subType; }
    public BigDecimal getFactorKgCo2e() { return factorKgCo2e; }
    public String getUnit() { return unit; }
    public String getSource() { return source; }

    public void setId(Long id) { this.id = id; }
    public void setCategory(Activity.Category category) { this.category = category; }
    public void setSubType(String subType) { this.subType = subType; }
    public void setFactorKgCo2e(BigDecimal factorKgCo2e) { this.factorKgCo2e = factorKgCo2e; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setSource(String source) { this.source = source; }
}