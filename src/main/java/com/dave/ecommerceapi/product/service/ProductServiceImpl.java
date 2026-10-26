package com.dave.ecommerceapi.product.service;

import com.dave.ecommerceapi.category.entity.Category;
import com.dave.ecommerceapi.category.exception.CategoryNotFoundException;
import com.dave.ecommerceapi.category.repository.CategoryRepository;
import com.dave.ecommerceapi.product.dto.request.ProductRequest;
import com.dave.ecommerceapi.product.dto.response.ProductPageResponse;
import com.dave.ecommerceapi.product.dto.response.ProductResponse;
import com.dave.ecommerceapi.product.entity.Product;
import com.dave.ecommerceapi.product.enums.ProductStatus;
import com.dave.ecommerceapi.product.exception.InvalidProductPaginationException;
import com.dave.ecommerceapi.product.exception.ProductAlreadyExistsException;
import com.dave.ecommerceapi.product.exception.ProductNotFoundException;
import com.dave.ecommerceapi.product.mapper.ProductMapper;
import com.dave.ecommerceapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    private static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "sku",
            "name",
            "price",
            "currency",
            "status",
            "createdAt",
            "updatedAt"
    );

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        String sku = normalizeSku(request.getSku());

        if (productRepository.existsBySku(sku)) {
            throw new ProductAlreadyExistsException(sku);
        }

        Category category = findCategory(request.getCategoryId());

        Product product = Product.builder()
                .sku(sku)
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .currency(request.getCurrency().trim())
                .category(category)
                .status(ProductStatus.DRAFT)
                .build();

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = findProduct(id);

        return productMapper.toResponse(product);
    }

    @Override
    public ProductPageResponse getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            Long categoryId,
            ProductStatus status
    ) {
        Pageable pageable = createPageable(page, size, sortBy, direction);

        if (categoryId != null) {
            if (categoryId <= 0) {
                throw new InvalidProductPaginationException(
                        "Category ID must be greater than zero."
                );
            }

            if (!categoryRepository.existsById(categoryId)) {
                throw new CategoryNotFoundException(categoryId);
            }
        }

        Page<Product> productPage;

        if (categoryId != null && status != null) {
            productPage = productRepository
                    .findByCategoryIdAndStatus(categoryId, status, pageable);
        } else if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else if (status != null) {
            productPage = productRepository.findByStatus(status, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return new ProductPageResponse(
                productPage.getContent()
                        .stream()
                        .map(productMapper::toResponse)
                        .toList(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);

        String sku = normalizeSku(request.getSku());

        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new ProductAlreadyExistsException(sku);
        }

        Category category = findCategory(request.getCategoryId());

        product.updateDetails(
                sku,
                request.getName().trim(),
                request.getDescription(),
                request.getPrice(),
                request.getCurrency().trim(),
                category
        );

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse changeProductStatus(Long id, ProductStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Product status is required.");
        }

        Product product = findProduct(id);
        product.changeStatus(status);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        if (page < 0) {
            throw new InvalidProductPaginationException(
                    "Page number cannot be negative."
            );
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidProductPaginationException(
                    "Page size must be between 1 and " + MAX_PAGE_SIZE + "."
            );
        }

        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidProductPaginationException(
                    "Invalid sort field. Allowed fields: "
                            + ALLOWED_SORT_FIELDS
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new InvalidProductPaginationException(
                    "Sort direction must be ASC or DESC."
            );
        }

        return PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );
    }

    private String normalizeSku(String sku) {
        return sku.trim().toUpperCase(Locale.ROOT);
    }

}
