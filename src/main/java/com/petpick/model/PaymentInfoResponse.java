package com.petpick.model;

import java.time.LocalDateTime;

public class PaymentInfoResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private String method;
    private int totalAmount;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    // Add other necessary fields based on Toss API response

    // Getters and Setters
    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }

    public void setMethod(String method) { this.method = method; }

    public int getTotalAmount() { return totalAmount; }

    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getRequestedAt() { return requestedAt; }

    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }

    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    // Add other getters and setters as needed
}

