package com.hotdeal.discord.infrastructure.crawler.exception;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;

/**
 * 크롤링 중 발생하는 HTTP 통신 관련 문제
 */
public class CrawlerHttpException extends ApiException {

    public CrawlerHttpException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CrawlerHttpException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public CrawlerHttpException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(errorCode, customMessage, cause);
    }
}
