package com.greenpulse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.EmissionFactor;
import com.greenpulse.repository.EmissionFactorRepository;

@RestController
@RequestMapping("/api/emission-factors")
public class EmissionFactorController {

    private final EmissionFactorRepository factorRepository;

    public EmissionFactorController(EmissionFactorRepository factorRepository) {
        this.factorRepository = factorRepository;
    }

    @GetMapping
    public ResponseEntity<List<EmissionFactor>> getAll() {
        return ResponseEntity.ok(factorRepository.findAll());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<EmissionFactor>> getByCategory(
            @PathVariable Category category) {
        return ResponseEntity.ok(factorRepository.findByCategory(category));
    }
}