package com.hotdeal.discord.infrastructure.crawler.jsoup;

import org.jsoup.nodes.Document;

/**
 * 지정된 URL의 HTML 문서를 가져오는 역할을 정의하는 인터페이스
 */
public interface HttpFetcher {

    /**
     * 주어진 URL에 접속하여 HTML Document 객체를 반환합니다.
     *
     * @param url 가져올 페이지의 URL
     * @return 해당 URL의 파싱된 Document 객체
     */
    Document fetch(String url);

}