package com.petpick.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT_001", "해당 상품 아이디는 존재하지 않습니다.", HttpStatus.NOT_FOUND);


    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
