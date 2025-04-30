package com.hotdeal.discord.infrastructure.discord.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotDealNotificationScheduler {

    private final HotDealNotificationSender notificationSender;

    // 5분마다 실행
    @Scheduled(fixedRate = 300000)
    public void scheduleHotDealNotifications() {
        log.info("핫딜 알림 스케줄러 실행 시작");
        notificationSender.processKeywordMatchNotifications();
        log.info("핫딜 알림 스케줄러 실행 완료");
    }

}
