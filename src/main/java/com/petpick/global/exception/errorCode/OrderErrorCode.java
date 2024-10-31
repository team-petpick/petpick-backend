package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND("ORDER_001", "해당 주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_COMPLETED("ORDER_002", "이미 완료된 주문입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK("ORDER_003", "상품의 재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED("ORDER_004", "결제에 실패하였습니다.", HttpStatus.PAYMENT_REQUIRED),
    INSUFFICIENT_FUNDS("ORDER_005", "잔액이 부족합니다.", HttpStatus.PAYMENT_REQUIRED),
    INVALID_PAYMENT_METHOD("ORDER_006", "유효하지 않은 결제 수단입니다.", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND("ORDER_007", "배송 주소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_MONTH_PARAMETER("ORDER_008", "유효하지 않은 개월 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
