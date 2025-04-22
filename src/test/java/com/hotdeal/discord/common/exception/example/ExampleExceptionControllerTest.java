package com.hotdeal.discord.common.exception.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.common.exception.ErrorResponse;
import com.hotdeal.discord.common.exception.GlobalExceptionHandler; // GlobalExceptionHandler 포함시키기 위함 (보통 자동 포함)
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // 결과 로그 출력
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // status(), jsonPath() 등

@WebMvcTest(ExampleExceptionController.class)
@Import(GlobalExceptionHandler.class)
public class ExampleExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExampleExceptionService exampleExceptionService;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("ID 포함 ExampleNotFoundException 발생 시 GlobalExceptionHandler가 처리하여 404 응답 반환")
    void triggerException_shouldReturn404_whenServiceThrowsExampleNotFoundExceptionWithId()
        throws Exception {

        // given
        var exceptionTriggerId = 999L;
        var expectedErrorCode = ErrorCode.EXAMPLE_NOT_FOUND;
        var expectedMessage = "ID가 " + exceptionTriggerId + "인 예시 데이터를 찾을 수 없습니다.";
        var expectedPath = "/test/exception/trigger/" + exceptionTriggerId;

        given(exampleExceptionService.processData(exceptionTriggerId)).willThrow(
            new ExampleNotFoundException(exceptionTriggerId));

        // when
        MvcResult result = mockMvc.perform(get("/test/exception/trigger/{id}", exceptionTriggerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse actualResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);

        assertThat(actualResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(actualResponse.getError()).isEqualTo(expectedErrorCode.getStatus().getReasonPhrase());
        assertThat(actualResponse.getCode()).isEqualTo(expectedErrorCode.getCode());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.getPath()).isEqualTo(expectedPath);
        assertThat(actualResponse.getFieldErrors()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("ExampleNotFoundException 발생 시 GlobalExceptionHandler가 처리하여 404 응답 반환")
    void triggerDefaultException_shouldReturn404_whenServiceThrowsDefaultExampleNotFoundException() throws Exception {
        // given
        var expectedErrorCode = ErrorCode.EXAMPLE_NOT_FOUND;
        var expectedMessage = expectedErrorCode.getMessage();
        var expectedPath = "/test/exception/trigger/default";

        when(exampleExceptionService.processDefault())
            .thenThrow(new ExampleNotFoundException());

        // when
        MvcResult result = mockMvc.perform(get("/test/exception/trigger/default") // GET 요청 보내기
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse actualResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);

        assertThat(actualResponse.getStatus()).isEqualTo(expectedErrorCode.getStatus().value());
        assertThat(actualResponse.getError()).isEqualTo(
            expectedErrorCode.getStatus().getReasonPhrase());
        assertThat(actualResponse.getCode()).isEqualTo(expectedErrorCode.getCode());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.getPath()).isEqualTo(expectedPath);
        assertThat(actualResponse.getFieldErrors()).isNotNull().isEmpty();
        assertThat(actualResponse.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("예외가 발생하지 않을 경우 200 OK 및 예상 문자열 응답 반환")
    void triggerException_shouldReturn200_whenNoException() throws Exception {
        // given
        var normalId = 1L;
        var serviceResult = "Data for ID " + normalId;
        var expectedResponseBody = "성공: " + serviceResult;

        given(exampleExceptionService.processData(normalId)).willReturn(serviceResult);

        // when
        MvcResult result = mockMvc.perform(get("/test/exception/trigger/{id}", normalId)
                .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andReturn();

        // then (검증)
        String actualResponseBody = result.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualTo(expectedResponseBody);
    }
}
