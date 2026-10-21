package com.dave.ecommerceapi.product.entity;

import com.dave.ecommerceapi.category.entity.Category;
import com.dave.ecommerceapi.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_products_sku",
                        columnNames = "sku"
                )
        },
        indexes = {
                @Index(
                        name = "idx_products_category_id",
                        columnList = "category_id"
                ),
                @Index(
                        name = "idx_products_status",
                        columnList = "status"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sku", nullable = false, length = 50)
    private String sku;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Builder
    private Product(
            String sku,
            String name,
            String description,
            BigDecimal price,
            String currency,
            Category category,
            ProductStatus status
    ) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency != null ? currency : "KES";
        this.category = category;
        this.status = status != null ? status : ProductStatus.DRAFT;
    }

    public void updateDetails(
            String name,
            String description,
            BigDecimal price,
            String currency,
            Category category
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.category = category;
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }
}