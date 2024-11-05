package com.petpick.model;

import com.petpick.domain.OrderDetail;
import com.petpick.domain.type.OrderDetailStatus;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private Integer orderDetailId;
    private Integer productId;
    private String productName;
    private String sellerStoreName;
    private Double orderDetailPrice;
    private Integer orderDetailCnt;
    private String productThumbnail;
    private OrderDetailStatus orderDetailStatus;

    public OrderDetailResponse(OrderDetail orderDetail, String productThumbnail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.productId = orderDetail.getProduct().getProductId();
        this.productName = orderDetail.getProduct().getProductName();
        this.sellerStoreName = orderDetail.getProduct().getSeller().getSellerStoreName();
        this.orderDetailPrice = orderDetail.getOrderDetailPrice();
        this.orderDetailCnt = orderDetail.getOrderDetailCnt();
        this.productThumbnail = productThumbnail;
        this.orderDetailStatus = orderDetail.getOrderDetailStatus();
    }
    public OrderDetailResponse() {
    }

}
