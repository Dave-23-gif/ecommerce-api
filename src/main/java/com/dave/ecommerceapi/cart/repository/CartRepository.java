package com.dave.ecommerceapi.cart.repository;

import com.dave.ecommerceapi.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart>findByCartToken(UUID cartToken);
    boolean existsByCartToken(UUID cartToken);
}
