package com.hotdeal.discord.infrastructure.discord.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

// Discord 메시지 전송 실패 시 발생 (주의: 비동기 콜백에서 발생 시 처리 한계 있음)
public class DiscordMessageSendException extends ApiException {
    public DiscordMessageSendException(ErrorCode errorCode) {
        super(errorCode);
    }
    public DiscordMessageSendException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(errorCode, customMessage, cause);
    }
    public DiscordMessageSendException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}