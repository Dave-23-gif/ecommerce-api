package com.dave.ecommerceapi.category.dto.response;

import java.time.OffsetDateTime;

public record CategoryResponse(Long id, String name, String description, OffsetDateTime createdAt,
                               OffsetDateTime updatedAt) {
}
