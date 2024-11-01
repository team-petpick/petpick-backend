package com.petpick.model;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer productId;
    private Integer cartCnt;
}
