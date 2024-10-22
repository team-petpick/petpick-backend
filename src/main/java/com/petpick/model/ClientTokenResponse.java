package com.petpick.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientTokenResponse {

    private String accessToken;

    private Long expiresIn;

    private String tokenType;
}
