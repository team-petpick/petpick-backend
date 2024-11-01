package com.petpick.service.cart;

import com.petpick.domain.Cart;
import com.petpick.domain.Product;
import com.petpick.domain.User;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.CartErrorCode;
import com.petpick.model.CartItemRequest;
import com.petpick.model.CartItemResponse;
import com.petpick.repository.CartRepository;
import com.petpick.repository.ProductImgRepository;
import com.petpick.repository.ProductRepository;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CartErrorCode.USER_NOT_FOUND));

        List<Cart> cartItems = cartRepository.findByUserUserId(userId);

        return cartItems.stream()
                .map(cart -> {
                    Integer productId = cart.getProduct().getProductId();
                    String thumbnailUrl = productImgRepository.findThumbnailByProductId(productId);
                    return new CartItemResponse(cart, thumbnailUrl);
                })
                .collect(Collectors.toList());
    }

    public void addCartItem(Integer userId, CartItemRequest cartItemRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CartErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new BaseException(CartErrorCode.PRODUCT_NOT_FOUND));

        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElse(Cart.builder()
                            .user(user)
                            .product(product)
                            .cartCnt(0)
                            .build());

        cartItem.setCartCnt(cartItem.getCartCnt() + cartItemRequest.getCartCnt());

        cartRepository.save(cartItem);
    }

    public void removeCartItem(Integer userId, Integer productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CartErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(CartErrorCode.PRODUCT_NOT_FOUND));

        cartRepository.deleteByUserAndProduct(user, product);
    }

    public void updateCartItem(Integer userId, Integer productId, Integer cnt) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CartErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(CartErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new BaseException(CartErrorCode.CART_ITEM_NOT_FOUND));

        cart.setCartCnt(cnt);

        cartRepository.save(cart);
    }
}
