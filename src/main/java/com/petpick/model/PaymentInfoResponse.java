package com.petpick.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentInfoResponse {
    // Getters and Setters
    private String paymentKey;
    private String orderId;
    private String status;
    private String method;
    private int totalAmount;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    // Add other necessary fields based on Toss API response

    // Add other getters and setters as needed
}

