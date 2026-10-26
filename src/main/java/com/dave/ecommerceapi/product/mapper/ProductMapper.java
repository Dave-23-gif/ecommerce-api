package com.dave.ecommerceapi.product.mapper;

import com.dave.ecommerceapi.product.dto.response.ProductResponse;
import com.dave.ecommerceapi.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName",source = "category.name")
    ProductResponse toResponse(Product product);
}
