package com.petpick.model;

import com.petpick.domain.Cart;
import lombok.Data;

@Data
public class CartItemResponse {
    private Integer productId;
    private String productName;
    private Integer cartCnt;
    private Integer productPrice;
    private String productThumbnail;
    private Integer productSale;
    private String sellerName;

    public CartItemResponse(Cart cart, String productThumbnail) {
        this.productId = cart.getProduct().getProductId();
        this.productName = cart.getProduct().getProductName();
        this.cartCnt = cart.getCartCnt();
        this.productPrice = cart.getProduct().getProductPrice();
        this.productThumbnail = productThumbnail;
        this.productSale = cart.getProduct().getProductSale();
        this.sellerName = cart.getProduct().getSeller().getSellerStoreName();
    }
}
