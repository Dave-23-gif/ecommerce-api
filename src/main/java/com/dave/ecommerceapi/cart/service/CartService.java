package com.dave.ecommerceapi.cart.service;

import com.dave.ecommerceapi.cart.dto.request.AddCartItemRequest;
import com.dave.ecommerceapi.cart.dto.request.UpdateCartItemRequest;
import com.dave.ecommerceapi.cart.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {

    CartResponse createCart();

    CartResponse getCart(UUID cartToken);

    CartResponse addItem(UUID cartToken, AddCartItemRequest request);

    CartResponse updateItem(
            UUID cartToken,
            Long itemId,
            UpdateCartItemRequest request
    );

    void removeItem(UUID cartToken, Long itemId);

    void clearCart(UUID cartToken);
}