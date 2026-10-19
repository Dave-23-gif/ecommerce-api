package com.dave.ecommerceapi.category.service;

import com.dave.ecommerceapi.category.dto.request.CategoryRequest;
import com.dave.ecommerceapi.category.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCatogories();
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);

    List<CategoryResponse> getAllCategories();
}
