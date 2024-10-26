package com.petpick.model;

import com.petpick.domain.Product;
import com.petpick.domain.type.PetKind;
import com.petpick.domain.type.ProductStatus;
import lombok.Data;

@Data
public class ProductResponse {
    private Integer productId;
    private String productName;
    private Integer productShare;
    private Integer productPrice;
    private Integer productCnt;
    private Integer productSale;
    private PetKind petKind;
    private ProductStatus productStatus;

    public ProductResponse(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productShare = product.getProductShare();
        this.productPrice = product.getProductPrice();
        this.productCnt = product.getProductCnt();
        this.productSale = product.getProductSale();
        this.petKind = product.getPetKind();
        this.productStatus = product.getProductStatus();
    }
}
