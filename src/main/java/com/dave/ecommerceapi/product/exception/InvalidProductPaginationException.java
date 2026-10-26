package com.dave.ecommerceapi.product.exception;

public class InvalidProductPaginationException extends RuntimeException {
    public InvalidProductPaginationException(String message) {
        super(message);
    }
}
