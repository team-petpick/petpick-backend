package com.petpick.model;

import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.domain.type.ProductStatus;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<ProductImgResponse> productImg; // Product Image

    public ProductListResponse(Product product, List<ProductImg> productImg) {
        this.productId = product.getProductId();
        this.category = new CategoryResponse(product.getCategory());
        this.productName = product.getProductName();
        this.seller = new SellerResponse(product.getSeller());
        this.productStatus = product.getProductStatus();
        this.productPrice = product.getProductPrice();
        this.productSale = product.getProductSale();
        this.productCnt = product.getProductCnt();
        this.productImg = productImg.stream().map(ProductImgResponse::new).collect(Collectors.toList());
    }
}
