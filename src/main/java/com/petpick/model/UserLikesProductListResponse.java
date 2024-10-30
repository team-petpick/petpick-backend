package com.petpick.model;

import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.domain.type.ProductStatus;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserLikesProductListResponse {
    private Integer productId;
    private CategoryResponse category;
    private String productName;
    private ProductStatus productStatus;
    private Integer productPrice;
    private Integer productSale;
    private Integer productCnt;
    private List<ProductImgResponse> productImg;

    public UserLikesProductListResponse(Product product, List<ProductImg> productImg) {
        this.productId = product.getProductId();
        this.category = new CategoryResponse(product.getCategory());
        this.productName = product.getProductName();
        this.productStatus = product.getProductStatus();
        this.productPrice = product.getProductPrice();
        this.productSale = product.getProductSale();
        this.productCnt = product.getProductCnt();
        this.productImg = productImg.stream().map(ProductImgResponse::new).collect(Collectors.toList());
    }
}
