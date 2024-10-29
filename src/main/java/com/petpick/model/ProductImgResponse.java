package com.petpick.model;

import com.petpick.domain.ProductImg;
import lombok.Data;

@Data
public class ProductImgResponse {
    private Integer productImgId;
    private String productImgUrl;
    private String productImgName;

    public ProductImgResponse(ProductImg productImg) {
        this.productImgId = productImg.getProductImgId();
        this.productImgUrl = productImg.getProductImgUrl();
        this.productImgName = productImg.getProductImgUrl();
    }
}
