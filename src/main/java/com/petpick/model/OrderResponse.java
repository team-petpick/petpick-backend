package com.petpick.model;


import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private Integer userId;
    private Integer addressId;
    private Integer orderPrice;
    private String paymentKey;
    private String orderStatus;
    private String orderRequest;
    private String createAt;
}

