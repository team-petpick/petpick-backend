package com.petpick.controller;

import com.petpick.model.CartItemRequest;
import com.petpick.model.CartItemResponse;
import com.petpick.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /*
    * Cart List
    * */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable Integer userId) {
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    /*
    * Add Product to cart
    * */
    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<Void> addCartItem(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @RequestBody CartItemRequest cartItemRequest) {
        cartService.addCartItem(userId, productId, cartItemRequest);
        return ResponseEntity.ok().build();
    }

    /*
    * Remove Product to Cart - entirely
    * */
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok().build();
    }
}
