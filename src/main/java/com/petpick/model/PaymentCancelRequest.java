package com.petpick.model;


import java.util.List;

public class PaymentCancelRequest {

    private String paymentKey;

    private String cancelReason;

    private List<OrderDetailCancelRequest> orderDetails;

    // Getters and Setters
    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<OrderDetailCancelRequest> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailCancelRequest> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "PaymentCancelRequest{" +
                "paymentKey='" + paymentKey + '\'' +
                ", cancelReason='" + cancelReason + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
