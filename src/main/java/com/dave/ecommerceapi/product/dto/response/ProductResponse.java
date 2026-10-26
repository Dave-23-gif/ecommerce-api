package com.dave.ecommerceapi.product.dto.response;

import com.dave.ecommerceapi.product.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(Long id, String sku, String name, String description, BigDecimal price, String currency, Long categoryId, String categoryName, ProductStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
