package com.greenpulse.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenpulse.dto.UpdateProfileDTO;
import com.greenpulse.model.User;
import com.greenpulse.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ── Get Profile ──────────────────────────────────────────
    public User getProfile(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // ── Update Profile ───────────────────────────────────────
    @Transactional
    public User updateProfile(String email, UpdateProfileDTO dto) {
        User user = getProfile(email);

        if (dto.getName()    != null) user.setName(dto.getName());
        if (dto.getCountry() != null) user.setCountry(dto.getCountry());
        if (dto.getBio()     != null) user.setBio(dto.getBio());
        if (dto.getDiet()    != null) user.setDiet(dto.getDiet());
        if (dto.getVehicle() != null) user.setVehicle(dto.getVehicle());

        return userRepository.save(user);
    }

    // ── Delete Account ───────────────────────────────────────
    @Transactional
    public void deleteAccount(String email) {
        userRepository.delete(getProfile(email));
    }
}