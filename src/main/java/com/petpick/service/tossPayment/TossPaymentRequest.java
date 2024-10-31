package com.petpick.service.tossPayment;

public class TossPaymentRequest {
    private String paymentKey;
    private int amount;
    private String orderId;

    public TossPaymentRequest(String paymentKey, int amount, String orderId) {
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.orderId = orderId;
    }

    // Getters and setters
    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) { this.amount = amount; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }
}
