// ── RegisterRequestDTO.java ──────────────────────────────────
package com.greenpulse.dto;

import com.greenpulse.model.User.DietType;
import com.greenpulse.model.User.VehicleType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
    private String country;
    private DietType diet;
    private VehicleType vehicle;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCountry() { return country; }
    public DietType getDiet() { return diet; }
    public VehicleType getVehicle() { return vehicle; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setCountry(String country) { this.country = country; }
    public void setDiet(DietType diet) { this.diet = diet; }
    public void setVehicle(VehicleType vehicle) { this.vehicle = vehicle; }
}