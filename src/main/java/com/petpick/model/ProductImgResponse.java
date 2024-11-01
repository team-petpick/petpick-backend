package com.petpick.model;

import com.petpick.domain.ProductImg;
import lombok.Data;

@Data
public class ProductImgResponse {
    private Integer productImgId;
    private Integer productImgThumb;
    private Integer descImgStatus; // Added field
    private String productImgUrl;
    private String productImgName;

    public ProductImgResponse(ProductImg productImg) {
        this.productImgId = productImg.getProductImgId();
        this.productImgThumb = productImg.getProductImgThumb();
        this.descImgStatus = productImg.getDescImgStatus(); // Initialize the new field
        this.productImgUrl = productImg.getProductImgUrl();
        this.productImgName = extractFileNameFromUrl(productImg.getProductImgUrl());
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
