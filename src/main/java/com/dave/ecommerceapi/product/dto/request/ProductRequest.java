package com.dave.ecommerceapi.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "SKU is required")
    @Size(max=50, message = "SKU must not exceed 50 characters")
    private String sku;
    @NotBlank(message = "Product name is required")
    @Size(max=150, message = "Product name must not exceed 150 characters")
    private String name;
    @Size(max=2000,message = "Description must not exceed 2000 characters")
    private String description;
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.00", message = "price must not be negative")
    private BigDecimal price;
    @NotBlank(message = "currency is required")
    @Pattern(regexp = "[A-Z]{3}", message = "Currency must a three-letter uppercase code")
    private String currency;
    @NotNull(message = "Category ID is required")
    private Long categoryId;


}
