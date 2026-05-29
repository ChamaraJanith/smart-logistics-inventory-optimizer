package com.optimizers.backend.service;

import java.util.Optional;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.request.PasswordChangeRequestDTO;
import com.optimizers.backend.entity.UserAccount;
import com.optimizers.backend.repository.UserRepository;
import com.optimizers.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    @Value("${default.admin.username}")
    private String defaultAdminUsername;

    @Value("${default.admin.password}")
    private String defaultAdminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

   @PostConstruct
    public void initializeDefaultUser() {
        Optional<UserAccount> existing =
                userRepository.findByUsername(defaultAdminUsername);

        if (existing.isEmpty()) {
            String passwordHash =
                    passwordEncoder.encode(defaultAdminPassword);

            UserAccount defaultUser =
                    new UserAccount(defaultAdminUsername, passwordHash);

            userRepository.save(defaultUser);
        }
    }

    public String authenticate(String username, String password) {
        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    @Transactional
    public void changePassword(String username, PasswordChangeRequestDTO request) {
        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (request.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
