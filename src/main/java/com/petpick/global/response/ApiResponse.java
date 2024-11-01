package com.petpick.global.response;

import lombok.Getter;

@Getter
public abstract class ApiResponse {
    private final boolean isSuccess;
    private final String code;
    private final String message;

    public ApiResponse(Boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}