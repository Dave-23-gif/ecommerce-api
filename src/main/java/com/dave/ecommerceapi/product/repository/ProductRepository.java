package com.dave.ecommerceapi.product.repository;

import com.dave.ecommerceapi.product.entity.Product;
import com.dave.ecommerceapi.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
    Page<Product>findByCategoryId(Long CategoryId, Pageable pageable);
    Page<Product>findByStatus(ProductStatus status, Pageable pageable);
    Page<Product>findByCategoryIdAndStatus(Long CategoryId, ProductStatus status, Pageable pageable);
}
