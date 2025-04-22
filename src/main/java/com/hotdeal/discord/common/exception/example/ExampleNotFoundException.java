package com.hotdeal.discord.common.exception.example;

import com.hotdeal.discord.common.exception.ApiException;
import com.hotdeal.discord.common.exception.ErrorCode;


public class ExampleNotFoundException extends ApiException {

    // 기본 생성자 에러 코드를 사용
    public ExampleNotFoundException() {
        super(ErrorCode.EXAMPLE_NOT_FOUND);
    }

    public ExampleNotFoundException(Long id) {
        super(ErrorCode.EXAMPLE_NOT_FOUND, "ID가 " + id + "인 예시 데이터를 찾을 수 없습니다.");
    }

    public ExampleNotFoundException(String message) {
        super(ErrorCode.EXAMPLE_NOT_FOUND, message);
    }

}