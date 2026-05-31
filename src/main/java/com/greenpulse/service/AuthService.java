package com.greenpulse.service;

import com.greenpulse.dto.AuthResponseDTO;
import com.greenpulse.dto.LoginRequestDTO;
import com.greenpulse.dto.RegisterRequestDTO;
import com.greenpulse.model.User;
import com.greenpulse.repository.UserRepository;
import com.greenpulse.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;
    private final JavaMailSender  mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       JavaMailSender mailSender) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
        this.mailSender      = mailSender;
    }

    // ── Register ─────────────────────────────────────────────
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO req) {
        if (userRepository.existsByEmail(req.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setCountry(req.getCountry());
        user.setDiet(req.getDiet());
        user.setVehicle(req.getVehicle());
        user.setEmailVerified(false);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        // user.getId() now returns String — no UUID conversion needed
        return new AuthResponseDTO(
            token,
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }

    // ── Login ────────────────────────────────────────────────
    public AuthResponseDTO login(LoginRequestDTO req) {
        User user = userRepository
            .findByEmail(req.getEmail().toLowerCase())
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        // user.getId() now returns String
        return new AuthResponseDTO(
            token,
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }

    // ── Forgot Password ──────────────────────────────────────
    @Transactional
    public void sendResetLink(String email) {
        userRepository.findByEmail(email.toLowerCase()).ifPresent(user -> {
            String resetToken = UUID.randomUUID().toString();
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("Green Pulse - Reset Your Password");
            msg.setText(
                "Hi " + user.getName() + ",\n\n" +
                "Click the link below to reset your password (expires in 1 hour):\n\n" +
                frontendUrl + "/reset-password?token=" + resetToken + "\n\n" +
                "If you did not request this, ignore this email.\n\n" +
                "-- The Green Pulse Team"
            );
            mailSender.send(msg);
        });
    }

    // ── Reset Password ───────────────────────────────────────
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid or expired token"));

        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        userRepository.save(user);
    }
}