package com.hotdeal.discord.infrastructure.crawler.fm;

import com.hotdeal.discord.infrastructure.crawler.service.HotDealScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FmScheduler implements HotDealScheduler {

    private final FmKoreaCrawlManager fmKoreaCrawlManager;

    @Scheduled(cron = "${community.fmkorea.cron}")
    public void scheduleFmKoreaCrawling() {
        log.info("[스케줄러] Fmkorea 핫딜 크롤링 스케줄 작업을 시작합니다.");
        try {
            fmKoreaCrawlManager.synchronizeFmKoreaDeals();
        } catch (Exception e) {
            log.error("[스케줄러] Fmkorea 크롤링 스케줄 작업 실행 중 오류 발생", e);
        } finally {
            log.info("[스케줄러] Fmkorea 핫딜 크롤링 스케줄 작업을 완료합니다.");
        }
    }
}

