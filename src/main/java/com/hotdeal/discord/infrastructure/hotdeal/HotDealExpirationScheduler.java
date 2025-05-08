package com.hotdeal.discord.infrastructure.hotdeal;

import com.hotdeal.discord.application.hotdeal.HotDealService;
import com.hotdeal.discord.infrastructure.crawler.service.HotDealScheduler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotDealExpirationScheduler implements HotDealScheduler {

    private final HotDealService hotDealService;

    @PostConstruct
    @Scheduled(cron = "${hotdeal.expiration.cron}")
    public void execute() {
        log.info("[핫딜 만료 스케줄러] 핫딜 상태 업데이트 작업을 시작합니다. (ACTIVE TO END)");
        try {
            hotDealService.expireOldHotDeals();
        } catch (Exception e) {
            log.error("[핫딜 만료 스케줄러] 핫딜 상태 업데이트 작업 실행 중 오류 발생 (ACTIVE TO END)", e);
        }
    }


}
