package com.petpick.model;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private Integer productId;
    private Integer orderDetailPrice;
    private Integer orderDetailCnt;
}

