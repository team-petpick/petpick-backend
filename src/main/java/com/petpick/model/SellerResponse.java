package com.petpick.model;

import com.petpick.domain.Seller;
import lombok.Data;

@Data
public class SellerResponse {
    private Integer sellerId;
    private String sellerStoreName;
    private String sellerNumber;
    private String sellerAddr;
    private String sellerAddrDetail;

    public SellerResponse(Seller seller) {
        this.sellerId = seller.getSellerId();
        this.sellerStoreName = seller.getSellerStoreName();
        this.sellerNumber = seller.getSellerNumber();
        this.sellerAddr = seller.getSellerAddr();
        this.sellerAddrDetail = seller.getSellerAddrDetail();
    }
}
