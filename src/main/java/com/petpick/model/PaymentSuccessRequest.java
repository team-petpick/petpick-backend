package com.petpick.model;

import com.petpick.domain.type.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaymentSuccessRequest {
    private Integer orderId;
    private String paymentKey;
    private Integer amount;
    private Integer userId;
    private String orderRequest;
    private String orderSerialCode;
    @Setter
    @Getter
    private OrderStatus orderStatus;

}
