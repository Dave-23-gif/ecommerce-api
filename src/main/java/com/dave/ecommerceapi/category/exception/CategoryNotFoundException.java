package com.dave.ecommerceapi.category.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id){
        super("Category with id " + id + " not found");
    }
}
