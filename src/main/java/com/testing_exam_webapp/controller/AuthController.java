package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.LoginRequest;
import com.example.hospital_db_backend.dto.LoginResponse;
import com.example.hospital_db_backend.dto.RegisterRequest;
import com.example.hospital_db_backend.model.mysql.User;
import com.example.hospital_db_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}

