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
    DISCORD_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "설정된 Discord 채널을 찾을 수 없거나 접근 권한이 없습니다."),
    DISCORD_LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "D002", "Discord 봇 로그인에 실패했습니다. 토큰을 확인하세요."),
    DISCORD_INITIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "D003", "Discord 봇 초기화에 실패했습니다."),
    DISCORD_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "D004", "Discord 메시지 전송 중 오류가 발생했습니다."),
    DISCORD_INVALID_CHANNEL_ID_FORMAT(HttpStatus.BAD_REQUEST, "D005", "Discord 채널 ID 형식이 잘못되었습니다.");

    // Add other domain errors

    private final HttpStatus status;
    private final String code;
    private final String message;
}
