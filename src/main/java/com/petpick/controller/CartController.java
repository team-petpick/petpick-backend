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
    @PostMapping("/{userId}")
    public ResponseEntity<String> addCartItem(
            @PathVariable Integer userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        cartService.addCartItem(userId, cartItemRequest);
        return ResponseEntity.ok("Successfully added cart item");
    }

    /*
    * Remove Product to Cart - entirely
    * */
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok("Successfully removed cart item");
    }

    /*
    * Update Product Count
    * */
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateCartItem(
            @PathVariable Integer userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        cartService.updateCartItem(userId, cartItemRequest.getProductId(), cartItemRequest.getCartCnt());
        return ResponseEntity.ok("Successfully updated cart item");
    }
}
