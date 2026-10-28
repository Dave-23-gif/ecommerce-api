
package com.dave.ecommerceapi.cart.controller;

import com.dave.ecommerceapi.cart.dto.request.AddCartItemRequest;
import com.dave.ecommerceapi.cart.dto.request.UpdateCartItemRequest;
import com.dave.ecommerceapi.cart.dto.response.CartResponse;
import com.dave.ecommerceapi.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart() {
        CartResponse response = cartService.createCart();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{cartToken}")
    public ResponseEntity<CartResponse> getCart(
            @PathVariable @NotNull UUID cartToken
    ) {
        return ResponseEntity.ok(
                cartService.getCart(cartToken)
        );
    }

    @PostMapping("/{cartToken}/items")
    public ResponseEntity<CartResponse> addItem(
            @PathVariable @NotNull UUID cartToken,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return ResponseEntity.ok(
                cartService.addItem(cartToken, request)
        );
    }

    @PatchMapping("/{cartToken}/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable @NotNull UUID cartToken,
            @PathVariable @Positive Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return ResponseEntity.ok(
                cartService.updateItem(cartToken, itemId, request)
        );
    }

    @DeleteMapping("/{cartToken}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable @NotNull UUID cartToken,
            @PathVariable @Positive Long itemId
    ) {
        cartService.removeItem(cartToken, itemId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartToken}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable @NotNull UUID cartToken
    ) {
        cartService.clearCart(cartToken);

        return ResponseEntity.noContent().build();
    }
}
