package com.hotdeal.discord.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 테스트용 에러 코드 추가
    EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "EX001", "예시 데이터를 찾을 수 없습니다."),

    // Common Errors
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 파라미터입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C004", "입력 값이 유효하지 않습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "타입이 유효하지 않습니다."),


    // Discord Errors
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "채널을 찾을 수 없습니다."),
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "D002", "이미 사용중인 이메일입니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "D003", "로그인 정보가 유효하지 않습니다.");

    // Add other domain errors

    private final HttpStatus status;
    private final String code;
    private final String message;
}
