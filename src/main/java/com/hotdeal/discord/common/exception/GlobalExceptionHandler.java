package com.hotdeal.discord.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 직접 정의한 API 예외 처리
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        log.warn("ApiException occurred: {} - {}", ex.getErrorCode(), ex.getMessage(), ex);
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode, request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * @Valid 어노테이션을 사용한 Request Body 또는 Parameter 검증 실패 시 처리
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        log.warn("MethodArgumentNotValidException occurred: {}", ex.getMessage(), ex);
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        // 또는 C001 INVALID_PARAMETER 사용 가능
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse response = ErrorResponse.of(errorCode, requestUri, ex.getBindingResult());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 지원하지 않는 HTTP 메서드 요청 시 처리
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        @NonNull HttpRequestMethodNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        log.warn("HttpRequestMethodNotSupportedException occurred: {}", ex.getMessage(), ex);
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        ErrorResponse response = ErrorResponse.of(errorCode, requestUri);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * JSON 파싱 실패 등 Request Body를 읽을 수 없을 때 처리
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        log.warn("HttpMessageNotReadableException occurred: {}", ex.getMessage(), ex);
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse response = ErrorResponse.of(errorCode, requestUri, "요청 본문의 형식이 잘못되었습니다."); // 좀 더 구체적인 메시지 제공 가능
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }


    /**
     * 위에서 처리하지 못한 모든 예외 처리
     * 내부 서버 오류로 간주하고 500 에러 반환
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception occurred: {}", ex.getMessage(), ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.of(errorCode, request.getRequestURI());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
