package com.hotdeal.discord.infrastructure.crawler.fm;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.infrastructure.crawler.dto.CrawledHotDealDto;
import com.hotdeal.discord.infrastructure.crawler.exception.CrawlerHttpException;
import com.hotdeal.discord.infrastructure.crawler.jsoup.HttpFetcher;
import com.hotdeal.discord.infrastructure.crawler.service.CommunityCrawler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FmKoreaCrawler implements CommunityCrawler {

    private final HttpFetcher httpFetcher;
    private final FmKoreaParser parser;
    private final FmProperties properties;

    @Override
    public List<CrawledHotDealDto> crawl() {
        List<CrawledHotDealDto> crawledList = new ArrayList<>();
        Set<String> processedPostIds = new HashSet<>();
        int totalPages = properties.getPagesToCrawl();

        log.info("[크롤러] Fmkorea 핫딜 크롤링 시작. 최대 {} 페이지 탐색 예정.", totalPages);

        for (int page = 1; page <= totalPages; page++) {
            String targetUrl = properties.getUrl() + "&page=" + page;
            Document doc;
            try {

                log.debug("[크롤러] 페이지 로딩 시도: {}", targetUrl);
                doc = httpFetcher.fetch(targetUrl);
                log.debug("[크롤러] 페이지 로딩 성공: {}", targetUrl);

            } catch (Exception e) {

                String errorMessage = String.format("페이지(%s) 로딩 실패", targetUrl);

                log.error("[크롤러] HTTP 요청 처리 중 오류 발생: Code={}, Message={}, Cause={}",
                    ErrorCode.CRAWLER_HTTP_FETCH_FAILED.getCode(),
                    errorMessage,
                    e.getMessage(),
                    e);

                // 예외가 발생했으나, 전체 크롤링을 중단하지 않고 다음 페이지 계속 크롤링
                continue;
            }

            String listItemSelector = properties.getListItemSelector();
            Elements listItems = doc.select(listItemSelector);
            if (listItems.isEmpty()) {
                log.info("[크롤러] 현재 페이지({})에 더 이상 게시물이 없어 크롤링을 중단합니다.", targetUrl);
                break;
            }

            List<CrawledHotDealDto> parsedItems = parser.parsePages(doc);
            int addedCount = 0;
            for (CrawledHotDealDto crawledInfo : parsedItems) {
                if (processedPostIds.add(crawledInfo.postId())) {
                    crawledList.add(crawledInfo);
                    addedCount++;
                }
            }
            log.info("[크롤러] 페이지 {} 파싱 완료. {}개 아이템 파싱, {}개 신규 추가됨.", page, parsedItems.size(), addedCount);

        } // 페이지 반복 종료

        crawledList.sort((o1, o2) -> {
            try {
                Long postId1 = Long.parseLong(o1.postId());
                Long postId2 = Long.parseLong(o2.postId());
                return postId1.compareTo(postId2);
            } catch (NumberFormatException e) {
                log.error("[크롤러] 정렬 중 Post ID를 숫자로 변환하는데 실패했습니다 (값1: '{}', 값2: '{}').", o1.postId(), o2.postId(), e);
                return 0;
            }
        });

        log.info("[크롤러] 크롤링 완료: 총 {}개의 유효한 핫딜 정보를 수집했습니다.", crawledList.size());
        return crawledList;
    }

}
