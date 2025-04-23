package com.hotdeal.discord.infrastructure.discord.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

/**
 * Discord 봇 초기화 실패 시 발생
 */
public class DiscordBotInitializationException extends ApiException {

    public DiscordBotInitializationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public DiscordBotInitializationException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(errorCode, customMessage, cause);
    }

    public DiscordBotInitializationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
