package com.hotdeal.discord.common.exception.example;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/exception")
@RequiredArgsConstructor
public class ExampleExceptionController {

    private final ExampleExceptionService exampleExceptionService;

    // ID를 받아 Service 호출, Service에서 예외 발생 가능
    @GetMapping("/trigger/{id}")
    public ResponseEntity<String> triggerException(@PathVariable Long id) {
        String result = exampleExceptionService.processData(id);
        return ResponseEntity.ok("성공: " + result);
    }

    // 기본 메시지를 사용하는 예외 발생용 엔드포인트
    @GetMapping("/trigger/default")
    public ResponseEntity<String> triggerDefaultException() {
        String result = exampleExceptionService.processDefault();
        return ResponseEntity.ok("성공: " + result);
    }
}