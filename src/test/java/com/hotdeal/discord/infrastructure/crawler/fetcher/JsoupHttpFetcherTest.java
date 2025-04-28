package com.hotdeal.discord.infrastructure.crawler.fetcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.infrastructure.crawler.exception.CrawlerHttpException;
import com.hotdeal.discord.infrastructure.crawler.properties.JsoupProperties;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsoupHttpFetcherTest {

    @Mock
    private JsoupProperties properties;
    @Mock
    private JsoupConnection jsoupConnection;
    @InjectMocks
    private JsoupHttpFetcher jsoupHttpFetcher;

    private final String TEST_URL = "http://example.com";
    private final String TEST_USER_AGENT = "test-agent";
    private final int TEST_TIMEOUT = 5000;

    @BeforeEach
    void setUp() {
        when(properties.getUserAgent()).thenReturn(TEST_USER_AGENT);
        when(properties.getTimeoutMs()).thenReturn(TEST_TIMEOUT);
    }

    @Test
    @DisplayName("성공적으로 Document를 가져와야 한다")
    void fetch_success() throws IOException {
        // given
        Document mockDocument = Jsoup.parse("<html><body>Test</body></html>");

        when(jsoupConnection.get(TEST_URL, TEST_USER_AGENT, TEST_TIMEOUT)).thenReturn(mockDocument);

        // when
        Document result = jsoupHttpFetcher.fetch(TEST_URL);

        assertNotNull(result);
        assertThat(result.body().text()).isEqualTo("Test");
    }

    @Test
    @DisplayName("SocketTimeoutException 발생 시 CrawlerHttpException (TIMEOUT) 을 던져야 한다")
    void fetch_timeoutException() throws IOException {
        // given
        when(jsoupConnection.get(TEST_URL, TEST_USER_AGENT, TEST_TIMEOUT))
            .thenThrow(new SocketTimeoutException("Test Timeout"));

        // when
        CrawlerHttpException exception = assertThrows(CrawlerHttpException.class, () -> {
            jsoupHttpFetcher.fetch(TEST_URL);
        });

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CRAWLER_CONNECTION_TIMEOUT);
        assertThat(exception.getErrorCode().getCode()).isEqualTo(
            ErrorCode.CRAWLER_CONNECTION_TIMEOUT.getCode());
        assertThat(exception.getErrorCode().getStatus()).isEqualTo(
            ErrorCode.CRAWLER_CONNECTION_TIMEOUT.getStatus());
        assertThat(exception.getErrorCode().getMessage()).isEqualTo(
            ErrorCode.CRAWLER_CONNECTION_TIMEOUT.getMessage());
    }
    @Test
    @DisplayName("IOException 발생 시 CrawlerHttpException (FETCH_FAILED) 을 던져야 한다")
    void fetch_ioException() throws IOException {
        // given
        when(jsoupConnection.get(TEST_URL, TEST_USER_AGENT, TEST_TIMEOUT))
            .thenThrow(new IOException("Test IO Error"));

        // when
        CrawlerHttpException exception = assertThrows(CrawlerHttpException.class, () -> {
            jsoupHttpFetcher.fetch(TEST_URL);
        });

         // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CRAWLER_HTTP_FETCH_FAILED);
        assertThat(exception.getErrorCode().getCode()).isEqualTo(
            ErrorCode.CRAWLER_HTTP_FETCH_FAILED.getCode());
        assertThat(exception.getErrorCode().getStatus()).isEqualTo(
            ErrorCode.CRAWLER_HTTP_FETCH_FAILED.getStatus());
        assertThat(exception.getErrorCode().getMessage()).isEqualTo(
            ErrorCode.CRAWLER_HTTP_FETCH_FAILED.getMessage());

    }
}