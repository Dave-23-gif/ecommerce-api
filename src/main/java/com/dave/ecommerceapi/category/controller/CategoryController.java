package com.dave.ecommerceapi.category.controller;

import com.dave.ecommerceapi.category.dto.request.CategoryRequest;
import com.dave.ecommerceapi.category.dto.response.CategoryPageResponse;
import com.dave.ecommerceapi.category.dto.response.CategoryResponse;
import com.dave.ecommerceapi.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request){
        CategoryResponse response= categoryService.createCategory(request);
        return ResponseEntity.created(java.net.URI.create("/api/v1/categories/" + response.id())).body(response);
    }
    @GetMapping("/{id}")
    public  ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        CategoryResponse response= categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<CategoryPageResponse> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        CategoryPageResponse response = categoryService.getAllCategories(
                page,
                size,
                sortBy,
                direction
        );
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        CategoryResponse response= categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
