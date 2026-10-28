package com.dave.ecommerceapi.cart.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        Long id,
        UUID cartToken,
        List<CartItemResponse> items,
        Integer totalItems,
        BigDecimal subtotal,
        String currency,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
