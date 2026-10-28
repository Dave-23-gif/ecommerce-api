package com.dave.ecommerceapi.cart.repository;

import com.dave.ecommerceapi.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByIdAndCartCartToken(Long id, UUID cartToken);
    boolean existsByCartIdAndProductId(Long cartId, Long productId);
}
