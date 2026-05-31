package com.greenpulse.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(length = 80)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DietType diet;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private VehicleType vehicle;

    @Column(length = 500)
    private String bio;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "password_reset_token", columnDefinition = "TEXT")
    private String passwordResetToken;

    @Column(name = "password_reset_expiry")
    private LocalDateTime passwordResetExpiry;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum DietType { OMNIVORE, FLEXITARIAN, VEGETARIAN, VEGAN }
    public enum VehicleType { NONE, ELECTRIC_CAR, HYBRID, PETROL_CAR, DIESEL_CAR, MOTORBIKE }

    // Auto generate UUID as string before saving
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public User() {}

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getCountry() { return country; }
    public DietType getDiet() { return diet; }
    public VehicleType getVehicle() { return vehicle; }
    public String getBio() { return bio; }
    public boolean isEmailVerified() { return emailVerified; }
    public String getPasswordResetToken() { return passwordResetToken; }
    public LocalDateTime getPasswordResetExpiry() { return passwordResetExpiry; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setCountry(String country) { this.country = country; }
    public void setDiet(DietType diet) { this.diet = diet; }
    public void setVehicle(VehicleType vehicle) { this.vehicle = vehicle; }
    public void setBio(String bio) { this.bio = bio; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    public void setPasswordResetToken(String passwordResetToken) { this.passwordResetToken = passwordResetToken; }
    public void setPasswordResetExpiry(LocalDateTime passwordResetExpiry) { this.passwordResetExpiry = passwordResetExpiry; }
}