package com.petpick.model;

import lombok.Data;

@Data
public class ProductLikesResponse {
    private Integer likesCount;

    public ProductLikesResponse(Integer likesCount) {
        this.likesCount = likesCount;
    }
}
