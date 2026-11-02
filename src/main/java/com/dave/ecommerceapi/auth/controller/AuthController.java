package com.dave.ecommerceapi.auth.controller;

import com.dave.ecommerceapi.auth.service.AuthService;
import com.dave.ecommerceapi.user.dto.request.LoginRequest;
import com.dave.ecommerceapi.user.dto.request.RegistrationRequest;
import com.dave.ecommerceapi.user.dto.response.AuthResponse;
import com.dave.ecommerceapi.user.dto.response.UserResponse;
import com.dave.ecommerceapi.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegistrationRequest request
    ) {
        UserResponse response = userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}