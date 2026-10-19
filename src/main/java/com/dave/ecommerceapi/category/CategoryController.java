package com.dave.ecommerceapi.category;

import com.dave.ecommerceapi.category.dto.request.CategoryRequest;
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
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        List<CategoryResponse> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
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
