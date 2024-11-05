package com.petpick.model;


import lombok.Getter;
import lombok.Setter;

/**
 * DTO for canceling an order detail.
 */
@Setter
@Getter
public class OrderDetailCancelRequest {

    // Getters and Setters
    private Integer orderDetailId;

    private Integer amount;

    private Double price;

    // Constructors
    public OrderDetailCancelRequest() {}

    public OrderDetailCancelRequest(Integer orderDetailId, Integer amount, Double price) {
        this.orderDetailId = orderDetailId;
        this.amount = amount;
        this.price = price;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "OrderDetailCancelRequest{" +
                "orderDetailId=" + orderDetailId +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
