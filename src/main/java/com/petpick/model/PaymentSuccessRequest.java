package com.petpick.model;

import com.petpick.domain.type.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class PaymentSuccessRequest {
    private String paymentKey;
    private Integer amount;
    private String orderRequest;
    private String orderSerialCode;
    private AddressRequest address;
    private List<OrderDetailRequest> orderDetails;
}

