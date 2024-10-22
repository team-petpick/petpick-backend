package com.petpick.model;

import lombok.Data;

@Data
public class ClientTokenResponse {

    private String accessToken;

    private int expiresIn;

    private String tokenType;
}
