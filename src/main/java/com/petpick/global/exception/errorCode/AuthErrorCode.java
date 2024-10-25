package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EMPTY_JWT("AUTH_001", "JWT가 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ID_TOKEN("AUTH_005", "유효하지 않은 ID TOKEN입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("AUTH_006", "유효하지 않은 ACCESS TOKEN입니다.", HttpStatus.BAD_REQUEST),
    FAILED_SOCIAL_LOGIN("AUTH_007", "소셜 로그인에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AUTHORIZATION_CODE("AUTH_008", "인가 코드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND("AUTH_009", "리프레시 토큰을 찾지 못햇습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("AUTH_010", "만료되거나 유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH("AUTH_011", "DB에서 리프레시 토큰을 찾지 못했습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_USER_EMAIL("AUTH_012", "해당 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);


    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
