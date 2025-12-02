package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.config.JwtTokenProvider;
import com.example.hospital_db_backend.dto.LoginRequest;
import com.example.hospital_db_backend.dto.LoginResponse;
import com.example.hospital_db_backend.dto.RegisterRequest;
import com.example.hospital_db_backend.model.mysql.User;
import com.example.hospital_db_backend.model.types.Role;
import com.example.hospital_db_backend.exception.UnauthorizedException;
import com.example.hospital_db_backend.exception.ValidationException;
import com.example.hospital_db_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        return new LoginResponse(token, user.getRole().name());
    }

    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ValidationException("Username already exists");
        }

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}

