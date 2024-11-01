package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements ErrorCode {
    USER_NOT_FOUND("CART_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("CART_002", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("CART_003", "장바구니 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY("CART_004", "유효하지 않은 수량입니다.", HttpStatus.BAD_REQUEST),
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
