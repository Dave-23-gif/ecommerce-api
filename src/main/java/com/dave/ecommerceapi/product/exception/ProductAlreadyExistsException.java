package com.dave.ecommerceapi.product.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String sku) {
        super("A product with SKU '" + sku + "' already exists");
    }
}
