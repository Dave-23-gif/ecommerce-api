package com.dave.ecommerceapi.cart.exceptions;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UUID cartToken) {
        super("Cart not found with token: "+ cartToken);
    }
}
