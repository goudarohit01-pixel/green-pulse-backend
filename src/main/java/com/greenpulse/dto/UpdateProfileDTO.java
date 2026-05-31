package com.greenpulse.dto;

import com.greenpulse.model.User.DietType;
import com.greenpulse.model.User.VehicleType;

public class UpdateProfileDTO {

    private String name;
    private String country;
    private String bio;
    private DietType diet;
    private VehicleType vehicle;

    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getBio() { return bio; }
    public DietType getDiet() { return diet; }
    public VehicleType getVehicle() { return vehicle; }

    public void setName(String name) { this.name = name; }
    public void setCountry(String country) { this.country = country; }
    public void setBio(String bio) { this.bio = bio; }
    public void setDiet(DietType diet) { this.diet = diet; }
    public void setVehicle(VehicleType vehicle) { this.vehicle = vehicle; }
}