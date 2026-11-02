package com.dave.ecommerceapi.user.dto.response;

import com.dave.ecommerceapi.user.entity.Role;

import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        OffsetDateTime createdAt
) {
}