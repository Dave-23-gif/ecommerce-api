package com.dave.ecommerceapi.category.service;

import com.dave.ecommerceapi.category.dto.request.CategoryRequest;
import com.dave.ecommerceapi.category.dto.response.CategoryPageResponse;
import com.dave.ecommerceapi.category.dto.response.CategoryResponse;
import com.dave.ecommerceapi.category.entity.Category;
import com.dave.ecommerceapi.category.exception.CategoryAlreadyExistsException;
import com.dave.ecommerceapi.category.exception.CategoryNotFoundException;
import com.dave.ecommerceapi.category.mapper.CategoryMapper;
import com.dave.ecommerceapi.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly =true)
public class CategoryServiceImpl implements  CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request){
        if(categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new CategoryAlreadyExistsException(request.getName());
        }
        Category category=new Category(request.getName(), request.getDescription());
        Category savedCategory=categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }
    @Override
    public CategoryResponse getCategoryById(Long id){
        Category category=categoryRepository.findById(id). orElseThrow(()-> new CategoryNotFoundException(id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryPageResponse getAllCatogories(int page, int size, String sortBy, String direction)
        {

            if (page < 0) {
                throw new IllegalArgumentException(
                        "Page number cannot be negative."
                );
            }

            if (size < 1 || size > 100) {
                throw new IllegalArgumentException(
                        "Page size must be between 1 and 100."
                );
            }

            if (!"id".equals(sortBy)
                    && !"name".equals(sortBy)
                    && !"createdAt".equals(sortBy)
                    && !"updatedAt".equals(sortBy)) {
                throw new IllegalArgumentException(
                        "Unsupported sort field. Allowed fields: id, name, createdAt, updatedAt."
                );
            }

            Sort.Direction sortDirection;

            try {
                sortDirection = Sort.Direction.fromString(direction);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Sort direction must be either ASC or DESC."
                );
            }

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection, sortBy)
            );

            Page<CategoryResponse> categoryPage = categoryRepository
                    .findAll(pageable)
                    .map(categoryMapper::toResponse);

            return new CategoryPageResponse(
                    categoryPage.getContent(),
                    categoryPage.getNumber(),
                    categoryPage.getSize(),
                    categoryPage.getTotalElements(),
                    categoryPage.getTotalPages(),
                    categoryPage.isFirst(),
                    categoryPage.isLast()
            );
        }



    @Override
    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request){
        Category category=categoryRepository.findById(id). orElseThrow(()-> new CategoryNotFoundException(id));
        if(categoryRepository.existsBynameIgnoreCaseAndIdNot(request.getName(), id)){
            throw new CategoryAlreadyExistsException(request.getName());
        }
        category.updateDetails(
                request.getName(), request.getDescription()
        );
        Category updatedCategory=categoryRepository.save(category);
        return  categoryMapper.toResponse(updatedCategory);
    }
    @Override
    @Transactional
    public void deleteCategory(Long id){
        Category category=categoryRepository.findById(id). orElseThrow(()-> new CategoryNotFoundException(id));
        categoryRepository.delete(category);
    }

}
