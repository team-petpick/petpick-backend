package com.petpick.model;

import com.petpick.domain.Product;
import com.petpick.domain.type.ProductStatus;
import lombok.Data;

@Data
public class ProductListResponse {
    private Integer productId;
    private CategoryResponse category; // Product Category
    private String productName;
    private SellerResponse seller; // Seller
    private ProductStatus productStatus; // ProductStatus
    private Integer productPrice;
    private Integer productSale;
    private Integer productCnt;
    // private ProductImgResponse productImg; // Product Image

    public ProductListResponse(Product product) {
        this.productId = product.getProductId();
        this.category = new CategoryResponse(product.getCategory());
        this.productName = product.getProductName();
        this.seller = new SellerResponse(product.getSeller());
        this.productStatus = product.getProductStatus();
        this.productPrice = product.getProductPrice();
        this.productSale = product.getProductSale();
        this.productCnt = product.getProductCnt();
    }
}
