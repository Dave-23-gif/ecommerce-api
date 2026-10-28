package com.dave.ecommerceapi.cart.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String sku,
        String productName,
        BigDecimal unitPrice,
        String currency,
        Integer quantity,
        BigDecimal subtotal
) {
}
