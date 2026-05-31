package com.greenpulse.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.EmissionFactor;
import com.greenpulse.repository.EmissionFactorRepository;

@Service
public class CarbonCalculatorService {

    private final EmissionFactorRepository factorRepository;

    public CarbonCalculatorService(EmissionFactorRepository factorRepository) {
        this.factorRepository = factorRepository;
    }

    /**
     * Core CO2 formula:
     * co2e_kg = quantity x emission_factor
     *
     * Example: 42 km x 0.21 kg/km = 8.82 kg CO2e
     */
    public BigDecimal calculate(Category category, String subType, BigDecimal quantity) {
        EmissionFactor factor = factorRepository
            .findByCategoryAndSubType(category, subType)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "No emission factor found for: " + category + " / " + subType));

        return quantity
            .multiply(factor.getFactorKgCo2e())
            .setScale(4, RoundingMode.HALF_UP);
    }

    public String getUnit(Category category, String subType) {
        return factorRepository
            .findByCategoryAndSubType(category, subType)
            .map(EmissionFactor::getUnit)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "No emission factor found for: " + category + " / " + subType));
    }
}