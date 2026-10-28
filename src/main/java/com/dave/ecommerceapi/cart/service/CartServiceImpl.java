package com.dave.ecommerceapi.cart.service;

import com.dave.ecommerceapi.cart.dto.request.AddCartItemRequest;
import com.dave.ecommerceapi.cart.dto.request.UpdateCartItemRequest;
import com.dave.ecommerceapi.cart.dto.response.CartItemResponse;
import com.dave.ecommerceapi.cart.dto.response.CartResponse;
import com.dave.ecommerceapi.cart.entity.Cart;
import com.dave.ecommerceapi.cart.entity.CartItem;
import com.dave.ecommerceapi.cart.exceptions.CartCurrencyMismatchException;
import com.dave.ecommerceapi.cart.exceptions.CartNotFoundException;
import com.dave.ecommerceapi.cart.exceptions.ProductNotAvailableException;
import com.dave.ecommerceapi.cart.repository.CartItemRepository;
import com.dave.ecommerceapi.cart.repository.CartRepository;
import com.dave.ecommerceapi.product.entity.Product;
import com.dave.ecommerceapi.product.enums.ProductStatus;
import com.dave.ecommerceapi.product.exception.ProductNotFoundException;
import com.dave.ecommerceapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartResponse createCart() {
        Cart cart = new Cart(UUID.randomUUID());

        Cart savedCart = cartRepository.save(cart);

        return toCartResponse(savedCart);
    }

    @Override
    public CartResponse getCart(UUID cartToken) {
        Cart cart = findCartByToken(cartToken);

        return toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(
            UUID cartToken,
            AddCartItemRequest request
    ) {
        Cart cart = findCartByToken(cartToken);

        Product product = productRepository.findById(request.productId())
                .orElseThrow(
                        () -> new ProductNotFoundException(request.productId())
                );

        validateProductAvailability(product);
        validateCurrency(cart, product);

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item ->
                        item.getProduct().getId().equals(product.getId())
                )
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = Math.addExact(
                    existingItem.getQuantity(),
                    request.quantity()
            );

            existingItem.updateQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem(
                    product,
                    request.quantity()
            );

            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);

        return toCartResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(
            UUID cartToken,
            Long itemId,
            UpdateCartItemRequest request
    ) {
        Cart cart = findCartByToken(cartToken);

        CartItem item = findCartItem(cartToken, itemId);

        item.updateQuantity(request.quantity());

        cartItemRepository.save(item);

        return toCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(UUID cartToken, Long itemId) {
        Cart cart = findCartByToken(cartToken);

        CartItem item = findCartItem(cartToken, itemId);

        cart.removeItem(item);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(UUID cartToken) {
        Cart cart = findCartByToken(cartToken);

        cart.clearItems();

        cartRepository.save(cart);
    }

    private Cart findCartByToken(UUID cartToken) {
        return cartRepository.findByCartToken(cartToken)
                .orElseThrow(
                        () -> new CartNotFoundException(cartToken)
                );
    }

    private CartItem findCartItem(UUID cartToken, Long itemId) {
        return cartItemRepository
                .findByIdAndCartCartToken(itemId, cartToken)
                .orElseThrow(
                        () -> new CartNotFoundException(cartToken)
                );
    }

    private void validateProductAvailability(Product product) {
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ProductNotAvailableException(
                    "Product is not available for purchase: "
                            + product.getSku()
            );
        }
    }

    private void validateCurrency(Cart cart, Product product) {
        String productCurrency = product.getCurrency()
                .trim()
                .toUpperCase(Locale.ROOT);

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                continue;
            }

            String existingCurrency = item.getProduct().getCurrency()
                    .trim()
                    .toUpperCase(Locale.ROOT);

            if (!existingCurrency.equals(productCurrency)) {
                throw new CartCurrencyMismatchException(
                        "All products in a cart must use the same currency"
                );
            }
        }
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();

        int totalItems = cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal subtotal = itemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String currency = itemResponses.isEmpty()
                ? null
                : itemResponses.getFirst().currency();

        return new CartResponse(
                cart.getId(),
                cart.getCartToken(),
                itemResponses,
                totalItems,
                subtotal,
                currency,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        Product product = item.getProduct();

        BigDecimal unitPrice = product.getPrice();

        BigDecimal itemSubtotal = unitPrice.multiply(
                BigDecimal.valueOf(item.getQuantity())
        );

        return new CartItemResponse(
                item.getId(),
                product.getId(),
                product.getSku(),
                product.getName(),
                unitPrice,
                product.getCurrency(),
                item.getQuantity(),
                itemSubtotal
        );
    }
}