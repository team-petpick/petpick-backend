package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT_001", "해당 상품 아이디는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_ACCESS_DENIED("PRODUCT_002", "해당 상품에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_PRODUCT_STATUS("PRODUCT_003", "유효하지 않은 상품 상태입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK("PRODUCT_004", "해당 상품의 재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    INVALID_SORT_PARAMETER("PRODUCT_005", "유효하지 않은 정렬 조건입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_PARAMETER("PRODUCT_006", "페이지를 생성하는데 실패했습니다.", HttpStatus.BAD_REQUEST),
    NO_PRODUCTS_AVAILABLE("PRODUCT_007", "조회 가능한 상품이 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TYPE_VALUE("PRODUCT_008", "상품의 타입이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_VALUE("PRODUCT_009", "해당 카테고리가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
