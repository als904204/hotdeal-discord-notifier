package com.hotdeal.discord.domain.keyword.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

/**
 * 사용자가 이미 등록한 키워드를 다시 등록하려고 할 때 발생하는 예외
 */
public class KeywordDuplicateException extends ApiException {

    public KeywordDuplicateException() {
        super(ErrorCode.DUPLICATE_KEYWORD);
    }

    public KeywordDuplicateException(String keyword) {
        super(ErrorCode.DUPLICATE_KEYWORD, "키워드 '" + keyword + "'는 이미 등록되어 있습니다.");
    }

    public KeywordDuplicateException(String message, Throwable cause) {
        super(ErrorCode.DUPLICATE_KEYWORD, message, cause);
    }
}
