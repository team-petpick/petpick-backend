package com.petpick.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentInfoResponse {
    private String paymentKey;
    private int orderId;
    private String status;
    private String method;
    private int totalAmount;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;

}

