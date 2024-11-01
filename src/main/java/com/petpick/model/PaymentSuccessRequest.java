package com.petpick.model;

import lombok.Data;

@Data
public class PaymentSuccessRequest {
    private String orderId;
    private String paymentKey;
    private Integer amount;
    private Integer userId;
    private String orderRequest;
}
