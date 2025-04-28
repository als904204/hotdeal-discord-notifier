package com.hotdeal.discord.domain.keyword.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;


public class KeywordNotFoundException extends ApiException {

    public KeywordNotFoundException() {
        super(ErrorCode.NOTFOUND_KEYWORD);
    }

    public KeywordNotFoundException(String keyword) {
        super(ErrorCode.NOTFOUND_KEYWORD, "키워드 '" + keyword + "'를 찾을 수 없습니다");
    }
    public KeywordNotFoundException(String message, Throwable cause) {
        super(ErrorCode.NOTFOUND_KEYWORD, message, cause);
    }

}
