package com.dave.ecommerceapi.category.repository;

import com.dave.ecommerceapi.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsBynameIgnoreCaseAndIdNot(String name, Long id);
}
