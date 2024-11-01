package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    FAILED_TO_GENERATE_JWT("AUTH_001", "JWT 생성 도중 실패했습니다..", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("AUTH_002", "유효하지 않은 액세스 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN("AUTH_003", "만료되거나 유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_AUTHORIZATION_CODE("AUTH_005", "인가 코드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FAILED_SOCIAL_LOGIN("AUTH_006", "소셜 로그인에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REFRESH_TOKEN_NOT_FOUND("AUTH_007", "리프레시 토큰을 찾지 못했습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_USER_EMAIL("AUTH_008", "해당 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND_FROM_DB("AUTH_009", "DB에서 해당 리프레시 토큰을 찾지 못했습니다.", HttpStatus.UNAUTHORIZED),

    ;


    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
