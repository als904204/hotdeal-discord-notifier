package com.hotdeal.discord.infrastructure.crawler.fetcher;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.infrastructure.crawler.exception.CrawlerHttpException;
import com.hotdeal.discord.infrastructure.crawler.properties.JsoupProperties;
import java.io.IOException;
import java.net.SocketTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsoupHttpFetcher implements HttpFetcher {

    private final JsoupProperties properties;
    private final JsoupConnection connection;

    @Override
    public Document fetch(String url) {
        try {
            Document elements = connection.get(url, properties.getUserAgent(),
                properties.getTimeoutMs());
            Response response = elements.connection().execute();
            if (response.statusCode() >= 400) {
                log.error("[HttpFetcher]HTTP error for URL: {}. Status code: {}", url, response.statusCode());
                throw new CrawlerHttpException(ErrorCode.CRAWLER_HTTP_FETCH_FAILED);
            }
            return response.parse();
        }catch (SocketTimeoutException e) {
            log.error("[HttpFetcher] Connection timeout for URL: {}. Error: {}", url, e.getMessage());
            throw new CrawlerHttpException(ErrorCode.CRAWLER_CONNECTION_TIMEOUT,
                "URL 접속 시간 초과: " + url, e);
        } catch (IOException e) {
            log.error("[HttpFetcher] Failed to fetch document from URL: {}. Error: {}", url, e.getMessage());
            throw new CrawlerHttpException(ErrorCode.CRAWLER_HTTP_FETCH_FAILED,
                "HTML 문서 가져오기 실패 (URL: " + url + ")", e);
        }
    }

}
