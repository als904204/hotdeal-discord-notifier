package com.hotdeal.discord.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    // HTTP Status Reason Phrase (e.g., "Bad Request", "Not Found")
    private String error;

    // Custom Error Code (e.g., "U001")
    private String code;

    // Error Message
    private String message;

    // Request URI
    private String path;

    // For validation errors
    private List<FieldErrorInfo> fieldErrors;

    // 생성자 (ApiException)
    private ErrorResponse(ErrorCode errorCode, String path, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().getReasonPhrase();
        this.code = errorCode.getCode();
        this.message = message;
        this.path = path;
        this.fieldErrors = new ArrayList<>();
    }

    // 생성자 (Validation Error)
    private ErrorResponse(ErrorCode errorCode, String path, BindingResult bindingResult) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().getReasonPhrase();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.path = path;
        this.fieldErrors = FieldErrorInfo.from(bindingResult);
    }

    // 생성자 (기타 Exception)
    private ErrorResponse(HttpStatus status, String code, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.code = code;
        this.message = message;
        this.path = path;
        this.fieldErrors = new ArrayList<>();
    }


    // 정적 팩토리 메소드 (ApiException 처리)
    public static ErrorResponse of(ErrorCode errorCode, String path, String message) {
        return new ErrorResponse(errorCode, path, message);
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(errorCode, path, errorCode.getMessage());
    }

    // 정적 팩토리 메소드 (Validation Error 처리)
    public static ErrorResponse of(ErrorCode errorCode, String path, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, path, bindingResult);
    }

    // 정적 팩토리 메소드 (기타 Exception 처리)
    public static ErrorResponse of(HttpStatus status, String code, String message, String path) {
        return new ErrorResponse(status, code, message, path);
    }


    // Validation 에러의 상세 정보를 담는 내부 클래스
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldErrorInfo {
        private String field;   // 필드명
        private String value;   // 입력된 값
        private String reason;  // 에러 이유

        private FieldErrorInfo(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldErrorInfo> from(BindingResult bindingResult) {
            if (bindingResult == null || !bindingResult.hasFieldErrors()) {
                return new ArrayList<>();
            }
            List<FieldError> errors = bindingResult.getFieldErrors();
            return errors.stream()
                .map(error -> new FieldErrorInfo(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()))
                .collect(Collectors.toList());
        }
    }
}
