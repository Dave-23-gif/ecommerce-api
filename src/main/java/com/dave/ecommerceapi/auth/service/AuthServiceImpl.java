package com.dave.ecommerceapi.auth.service;

import com.dave.ecommerceapi.security.jwt.JwtService;
import com.dave.ecommerceapi.user.dto.request.LoginRequest;
import com.dave.ecommerceapi.user.dto.response.AuthResponse;
import com.dave.ecommerceapi.user.entity.User;
import com.dave.ecommerceapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final long tokenExpiration;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService,
            @Value("${app.security.jwt.expiration}") long tokenExpiration
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.password()
                )
        );

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user was not found")
                );

        String accessToken = jwtService.generateToken(user);

        return new AuthResponse(
                accessToken,
                "Bearer",
                tokenExpiration
        );
    }
}