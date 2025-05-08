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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotDealNotificationSender {

    private final HotDealService hotDealService;
    private final NotificationRecordRepository notificationRecordRepository;
    private final KeywordSubscriptionRepository keywordSubscriptionRepository;
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

                NotificationRecord record = NotificationRecord.builder()
                    .keywordId(keywordId)
                    .hotDealId(hotDealId)
                    .build();

                try{
                    notificationRecordRepository.save(record);
                    var msg = hotDealService.buildHotDealMessage(Collections.singletonList(deal));
                    discordMessageSender.sendDM(subscription.getDiscordUserId(), msg);
                    log.info("사용자 {} 에게 핫딜 {} 알림 전송 완료 (upsert)", subscription.getDiscordUserId(),
                        deal.getTitle());
                } catch (DataIntegrityViolationException e) {
                    // 유니크 제약조건 위반 시 이미 알림을 보낸 것이므로 무시
                    log.debug("사용자 {} 에게 핫딜 {} 알림이 이미 전송됨", subscription.getDiscordUserId(), deal.getTitle());
                }

            }
        }
    }

}
