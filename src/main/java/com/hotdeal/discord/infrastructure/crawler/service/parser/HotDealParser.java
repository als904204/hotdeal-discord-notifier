package com.hotdeal.discord.infrastructure.crawler.service.parser;

import com.hotdeal.discord.infrastructure.crawler.dto.CrawledHotDealDto;
import java.util.List;
import org.jsoup.nodes.Document;

/**
 * Parser (파서)
 * 역할: HTML 문서를 분석하여 필요한 정보를 추출하는 책임
 * 주요 기능: HTML 태그에서 원하는 데이터(제목, 가격, URL 등)를 선택하고 추출
 * 입력: HTML Document 객체
 * 출력: 파싱된 정보를 담은 DTO 객체 리스트
 * 특징: 사이트별 HTML 구조에 의존적이며, DOM 요소 선택과 데이터 정제에 집중
 */
public interface HotDealParser {
    List<CrawledHotDealDto> parsePages(Document document);
}
