package com.petpick.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ErrorResponse extends ApiResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String errorMessage;

    public ErrorResponse(String errorCode, String errorMessage) {
        super(false, errorCode, "ERROR");
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse error(String errorCode, String errorMessage) {
        return new ErrorResponse(errorCode, errorMessage);
    }
}