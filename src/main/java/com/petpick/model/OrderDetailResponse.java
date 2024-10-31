package com.petpick.model;

import com.petpick.domain.OrderDetail;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private Integer orderDetailId;
    private Integer productId;
    private String productName;
    private String sellerName;
    private Double orderDetailPrice;
    private Integer orderDetailCnt;
    private String productThumbnail;

    public OrderDetailResponse(OrderDetail orderDetail, String productThumbnail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.productId = orderDetail.getProduct().getProductId();
        this.productName = orderDetail.getProduct().getProductName();
        this.sellerName = orderDetail.getProduct().getSeller().getSellerStoreName();
        this.orderDetailPrice = orderDetail.getOrderDetailPrice();
        this.orderDetailCnt = orderDetail.getOrderDetailCnt();
        this.productThumbnail = productThumbnail;
    }
}
