package com.dave.ecommerceapi.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @Size(max = 500)
    private String description;

}
