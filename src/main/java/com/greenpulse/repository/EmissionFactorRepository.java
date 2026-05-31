package com.greenpulse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenpulse.model.Activity.Category;
import com.greenpulse.model.EmissionFactor;

@Repository
public interface EmissionFactorRepository extends JpaRepository<EmissionFactor, Long> {
    Optional<EmissionFactor> findByCategoryAndSubType(Category category, String subType);
    List<EmissionFactor> findByCategory(Category category);
}