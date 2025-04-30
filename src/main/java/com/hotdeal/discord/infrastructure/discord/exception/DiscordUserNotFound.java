package com.hotdeal.discord.infrastructure.discord.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

public class DiscordUserNotFound extends ApiException {

    public DiscordUserNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
    public DiscordUserNotFound(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
    public DiscordUserNotFound(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
