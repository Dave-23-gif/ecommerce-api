package com.dave.ecommerceapi.category.service;

import com.dave.ecommerceapi.category.dto.request.CategoryRequest;
import com.dave.ecommerceapi.category.dto.response.CategoryResponse;
import com.dave.ecommerceapi.category.entity.Category;
import com.dave.ecommerceapi.category.exception.CategoryAlreadyExistsException;
import com.dave.ecommerceapi.category.exception.CategoryNotFoundException;
import com.dave.ecommerceapi.category.mapper.CategoryMapper;
import com.dave.ecommerceapi.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
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
    public List<CategoryResponse> getAllCatogories() {
        return List.of();
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
