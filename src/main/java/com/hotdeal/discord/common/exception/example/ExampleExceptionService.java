package com.hotdeal.discord.common.exception.example;


import org.springframework.stereotype.Service;

@Service
public class ExampleExceptionService {

    // 특정 ID일 경우 예외 발생시키는 메서드
    public String processData(Long id) {

        // 999번 ID일 경우 ID 포함 예외 발생
        if (id != null && id == 999L) {
            throw new ExampleNotFoundException(id);
        } else if (id != null && id == 888L) {
            // 888번 ID일 경우 커스텀 메세지 포함 예외 발생
            throw new ExampleNotFoundException("Custom Message: Data not found for ID " + id);
        }
        // 예외 발생 안 하면 더미 데이터 반환
        return "Data for ID " + id;
    }

    // 기본 메시지 예외 발생시키는 메서드
    public String processDefault() {
        throw new ExampleNotFoundException();
    }
}
