package com.cee.business_match_backend.auth.service;

import com.cee.business_match_backend.auth.dto.AuthResponse;
import com.cee.business_match_backend.auth.dto.RegisterRequest;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .fullName(request.getFullName())
                .companyName(request.getCompanyName())
                .country(request.getCountry())
                .description(request.getDescription())
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }

}
