package com.petpick.global.exception.errorCode;

import com.petpick.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LikesErrorCode implements ErrorCode {
    ALREADY_LIKE_PRODUCT("LIKES_001", "이미 좋아요를 누른 상품입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_UNLIKE_PRODUCT("LIKES_002", "이미 좋아요를 해제한 상태입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
