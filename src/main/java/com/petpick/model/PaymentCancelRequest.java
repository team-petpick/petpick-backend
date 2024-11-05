package com.petpick.model;

import lombok.Data;

@Data
public class PaymentCancelRequest {
    private Integer orderDetailId;
    private Integer orderDetailCnt; // The quantity to cancel
    private Integer orderId;        // The ID of the order
    private String cancelReason;
}