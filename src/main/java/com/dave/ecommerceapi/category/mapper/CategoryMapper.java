package com.dave.ecommerceapi.category.mapper;

import com.dave.ecommerceapi.category.dto.response.CategoryResponse;
import com.dave.ecommerceapi.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}
