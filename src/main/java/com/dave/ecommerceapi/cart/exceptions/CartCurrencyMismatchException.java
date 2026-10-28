package com.dave.ecommerceapi.cart.exceptions;

public class CartCurrencyMismatchException extends RuntimeException {
    public CartCurrencyMismatchException(String message) {
        super(message);
    }
}
