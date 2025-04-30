package com.hotdeal.discord.infrastructure.discord.notification;

import com.hotdeal.discord.application.hotdeal.HotDealService;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import com.hotdeal.discord.domain.notification.NotificationRecord;
import com.hotdeal.discord.infrastructure.persistence.keyword.KeywordSubscriptionRepository;
import com.hotdeal.discord.infrastructure.persistence.notification.NotificationRecordRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotDealNotificationSender {

    private final HotDealService hotDealService;
    private final KeywordSubscriptionRepository keywordSubscriptionRepository;
    private final NotificationRecordRepository notificationRecordRepository;
    private final DiscordMessageSender discordMessageSender;

    public void processKeywordMatchNotifications() {

        // TODO : 현재는 진행중인 핫딜 모두 조회임 추후 개선
        List<HotDeal> activeHotDeals = hotDealService.findActiveHotDeals();

        if (activeHotDeals.isEmpty()) {
            log.info("활성 상태인 핫딜이 없습니다. 알림 프로세스를 종료합니다.");
            return;
        }

        // TODO : 현재는 등록된 키워드 모두 조회임 추후 개선
        List<KeywordSubscription> allSubscriptions = keywordSubscriptionRepository.findAll();

        if (allSubscriptions.isEmpty()) {
            log.info("키워드 구독 정보가 없습니다. 알림 프로세스를 종료합니다.");
            return;
        }

        // TODO : 키워드 매칭 로직 개선 필요, 현재 모든 구독자에 대해 동일한 핫딜 목록을 반복적으로 필터링중
        for (KeywordSubscription subscription : allSubscriptions) {

            // 키워드에 맞는 핫딜 필터링
            List<HotDeal> matchingDeals = hotDealService.filterMatchingHotDeals(activeHotDeals,
                subscription.getKeyword());

            if (matchingDeals.isEmpty()) {
                continue;
            }

            for(HotDeal deal : matchingDeals) {
                Long keywordId = subscription.getId();
                Long hotDealId = deal.getId();

                boolean alreadySent = notificationRecordRepository.existsByKeywordIdAndHotDealId(
                    keywordId, hotDealId);

                if (alreadySent) {
                    log.debug("키워드 {} 에 대해 핫딜 {} 알림 이미 전송됨. 건너뜁니다.",
                        keywordId, hotDealId);
                    continue;
                }

                try {
                    var msg = hotDealService.buildHotDealMessage(Collections.singletonList(deal));
                    discordMessageSender.sendDM(subscription.getDiscordUserId(), msg);

                    NotificationRecord record = NotificationRecord.builder()
                        .keywordId(keywordId)
                        .hotDealId(hotDealId)
                        .build();

                    notificationRecordRepository.save(record);

                    log.debug("사용자 {}, 핫딜 {} 알림 전송 완료 및 기록 저장", subscription.getDiscordUserId(),
                        hotDealId);
                } catch (Exception e) {
                    log.error("사용자 {} 핫딜 {} 알림 전송 중 오류 발생: {}",
                        subscription.getDiscordUserId(), hotDealId, e.getMessage());
                }

            }
        }
    }


}
