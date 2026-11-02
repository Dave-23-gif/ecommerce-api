package com.dave.ecommerceapi.user.dto.response;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}