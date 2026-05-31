package com.greenpulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8)
    private String newPassword;

    public String getToken() { return token; }
    public String getNewPassword() { return newPassword; }
    public void setToken(String token) { this.token = token; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}