package com.hotdeal.discord.infrastructure.crawler.service.crawl;

import com.hotdeal.discord.infrastructure.crawler.dto.CrawledHotDealDto;
import java.util.List;

/**
 * Crawler (크롤러)
 * 역할: 전체 크롤링 프로세스 조정 및 관리
 * 주요 기능: URL 연결, HTTP 요청 수행, Parser 호출, 결과 반환
 * 입력: 없음 (또는 크롤링할 URL, 페이지 수 등 매개변수)
 * 출력: 크롤링된 결과물(파싱된 DTO 객체들)
 * 특징: 외부 시스템과의 통신 관리, 크롤링 흐름 제어
 */
public interface CommunityCrawler {
    List<CrawledHotDealDto> crawl();
}