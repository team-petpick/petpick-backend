package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EMPTY_MEMBER("USER_001", "존재하지 않는 사용자입니다.", HttpStatus.CONFLICT),
    UN_REGISTERED_MEMBER("USER_002", "", HttpStatus.OK),
    DUPLICATE_MEMBER("USER_003", "중복된 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("USER_004", "중복된 닉네임입니다.", HttpStatus.CONFLICT),
    INVALID_MEMBER("USER_005", "올바르지 않은 사용자입니다.", HttpStatus.BAD_REQUEST),
    DEFAULT_PROFILE("USER_006", "기본 이미지는 삭제할 수 없습니다.", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
