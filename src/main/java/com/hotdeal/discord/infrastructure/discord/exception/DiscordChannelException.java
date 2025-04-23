package com.hotdeal.discord.infrastructure.discord.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

/**
 * Discord 채널 관련 문제 발생 시 (찾을 수 없거나, ID 형식 오류 등)
 */
public class DiscordChannelException extends ApiException {

    public DiscordChannelException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DiscordChannelException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public DiscordChannelException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(errorCode, customMessage, cause);
    }

}
