package com.petpick.controller;

import com.petpick.model.CartItemRequest;
import com.petpick.model.CartItemResponse;
import com.petpick.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /*
    * Cart List
    * */
    @GetMapping("/cart")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestAttribute Integer userId) {
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    /*
    * Add Product to cart
    * */
    @PostMapping("/cart")
    public ResponseEntity<String> addCartItem(
            @RequestAttribute Integer userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        cartService.addCartItem(userId, cartItemRequest);
        return ResponseEntity.ok("Successfully added cart item");
    }

    /*
    * Remove Product to Cart - entirely
    * */
    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<String> removeCartItem(
            @RequestAttribute Integer userId,
            @PathVariable Integer productId) {
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok("Successfully removed cart item");
    }

    /*
    * Update Product Count
    * */
    @PatchMapping("/cart")
    public ResponseEntity<String> updateCartItem(
            @RequestAttribute Integer userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        cartService.updateCartItem(userId, cartItemRequest.getProductId(), cartItemRequest.getCartCnt());
        return ResponseEntity.ok("Successfully updated cart item");
    }
}
