package com.dave.ecommerceapi.product.service;

import com.dave.ecommerceapi.product.dto.request.ProductRequest;
import com.dave.ecommerceapi.product.dto.response.ProductPageResponse;
import com.dave.ecommerceapi.product.dto.response.ProductResponse;
import com.dave.ecommerceapi.product.enums.ProductStatus;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    ProductPageResponse getAllProducts(int page, int size, String sortBy, String direction, Long categoryId, ProductStatus status);
    ProductResponse updateProduct(Long id,ProductRequest request);
    ProductResponse changeProductStatus(Long id, ProductStatus status);
    void deleteProduct(Long id);
}
