package com.hotdeal.discord.infrastructure.crawler.service.parser;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.domain.hotdeal.CommunityType;
import com.hotdeal.discord.domain.hotdeal.HotDealStatus;
import com.hotdeal.discord.infrastructure.crawler.dto.CrawledHotDealDto;
import com.hotdeal.discord.infrastructure.crawler.exception.CrawlerHttpException;
import com.hotdeal.discord.infrastructure.crawler.properties.FmProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FmKoreaParser implements HotDealParser {

    private final FmProperties properties;
    private static final CommunityType FM_KOREA = CommunityType.FMKOREA;
    private static final Pattern DOC_SRL_PATTERN = Pattern.compile("[?&]document_srl=(\\d+)");

    /**
     * 주어진 HTML Document 객체에서 핫딜 목록을 파싱합니다.
     *
     * @param doc 파싱할 Jsoup Document 객체
     * @return 파싱된 CrawledInfo 객체 리스트
     */
    @Override
    public List<CrawledHotDealDto> parsePages(Document doc) {
        List<CrawledHotDealDto> results = new ArrayList<>();
        String listItemSelector = properties.getListItemSelector();
        String linkSelector = properties.getLinkSelector();
        String endedDealClass = properties.getEndedDealClass();

        Elements listItems = doc.select(listItemSelector);
        log.debug("[파서] 페이지 내 아이템 {}개 파싱 시작 (선택자: {})", listItems.size(), listItemSelector);

        for (Element item : listItems) {
            parseHotDealItem(item, linkSelector, endedDealClass)
                .ifPresent(results::add);
        }

        log.debug("[파서] 페이지 파싱 완료: {}개의 유효한 핫딜 발견.", results.size());
        return results;
    }

    /**
     * 개별 핫딜 아이템(HTML Element)을 파싱하여 CrawledInfo 객체를 생성합니다.
     *
     * @param item           파싱할 핫딜 아이템 Element 객체
     * @param linkSelector   링크 요소를 찾기 위한 CSS 선택자
     * @param endedDealClass 종료된 딜을 식별하는 CSS 클래스명
     * @return 파싱된 정보가 담긴 Optional<CrawledInfo>, 파싱 실패 시 Optional.empty() 반환
     */
    private Optional<CrawledHotDealDto> parseHotDealItem(Element item, String linkSelector, String endedDealClass) {
        Element linkElement = item.selectFirst(linkSelector);
        if (linkElement == null) {
            log.warn("[파서] 링크 요소를 찾지 못했습니다 (선택자: '{}'). 해당 아이템은 건너<0xEB>니다.", linkSelector);
            // 개별 아이템 파싱 실패 시, 로그 남기고 Optional.empty() 반환하여 해당 아이템만 제외
            return Optional.empty();
        }

        String title = linkElement.ownText().trim();
        if (title.isEmpty()) {
            title = linkElement.text().trim();
        }
        String url = linkElement.absUrl("href");
        String postId;
        try {
            postId = extractPostIdFromUrl(url);
        } catch (RuntimeException e) {
            log.warn("[파서] Post ID 추출 실패 (URL: '{}'). 아이템 건너<0xEB>니다. 원인: {}", url, e.getMessage());
            // 개별 아이템 파싱 실패 시, 로그 남기고 Optional.empty() 반환
            return Optional.empty();
        }

        boolean isEnded = linkElement.hasClass(endedDealClass);
        HotDealStatus currentStatus = isEnded ? HotDealStatus.END : HotDealStatus.ACTIVE;

        if (title.isEmpty() || url.isEmpty()) {
            log.warn("[파서] 필수 정보(제목 또는 URL)가 부족합니다. 아이템 건너<0xEB>니다 (PostId: {}).", postId);
            // 개별 아이템 파싱 실패 시, 로그 남기고 Optional.empty() 반환
            return Optional.empty();
        }

        return Optional.of(new CrawledHotDealDto(FM_KOREA.getDescription(), postId, title, url, currentStatus));
    }

    /**
     * URL에서 게시글 ID를 추출합니다.
     * 1순위: document_srl 쿼리 파라미터
     * 2순위: URL 경로의 마지막 숫자 세그먼트
     */
    private String extractPostIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new CrawlerHttpException(ErrorCode.CRAWLER_URL_PARSING_FAILED,
                "URL : " + url);
        }

        Matcher matcher = DOC_SRL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        String path = url.split("[?#]")[0];
        int lastSlashIndex = path.lastIndexOf('/');

        if (lastSlashIndex == -1 || lastSlashIndex == path.length() - 1) {
            throw new CrawlerHttpException(ErrorCode.CRAWLER_URL_PARSING_FAILED,
                "URL : " + url);
        }

        String lastSegment = path.substring(lastSlashIndex + 1);

        if (lastSegment.matches("\\d+")) {
            return lastSegment;
        } else {
            throw new CrawlerHttpException(ErrorCode.CRAWLER_URL_PARSING_FAILED,
                "URL : " + url);
        }
    }
}
