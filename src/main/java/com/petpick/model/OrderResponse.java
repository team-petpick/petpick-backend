package com.petpick.model;

import com.petpick.domain.Orders;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer ordersId;
    private Integer ordersPrice;
    private LocalDateTime orderCreateAt;
    private String ordersStatus;
    private List<OrderDetailResponse> orderDetails;

    public OrderResponse(Orders order, List<OrderDetailResponse> orderDetails) {
        this.ordersId = order.getOrdersId();
        this.ordersPrice = order.getOrdersPrice();
        this.orderCreateAt = order.getCreateAt();
        this.ordersStatus = order.getOrdersStatus().name();
        this.orderDetails = orderDetails;
    }
}