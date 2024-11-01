package com.petpick.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();

    String getMessage();

    HttpStatus getStatus();
}
