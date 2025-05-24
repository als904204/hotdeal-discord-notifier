package com.hotdeal.discord.infrastructure.discord.notification;

import com.hotdeal.discord.application.hotdeal.HotDealService;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import com.hotdeal.discord.domain.notification.NotificationRecord;
import com.hotdeal.discord.infrastructure.persistence.keyword.KeywordSubscriptionRepository;
import com.hotdeal.discord.infrastructure.persistence.notification.NotificationRecordRepository;
import java.util.ArrayList;
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

    /**
     * 1. 활성 핫딜을 조회
     * 2. 구독 중인 키워드와 매칭되는 핫딜을 찾는다.
     * 3. 해당 사용자에게 알림을 전송한다.
     */
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

        // (사용자 ID, 키워드) 기준으로 매칭되는 핫딜들을 묶어서 처리
        allSubscriptions.forEach(subscription -> {

            // 해당 구독 키워드에 맞는 핫딜 필터링
            List<HotDeal> matchingDeals = hotDealService.filterMatchingHotDeals(
                activeHotDeals,
                subscription.getKeyword());

            if (matchingDeals.isEmpty()) {
                return;
            }

            var matchingDealIds = matchingDeals.stream()
                .map(HotDeal::getId)
                .toList();

            var alreadyNotifiedHotDealIds = notificationRecordRepository.NotifiedHotDealIdsByKeywordIdAndHotDealIdsIn(
                subscription.getId(), matchingDealIds
            );

            var newDealsToNotify = matchingDeals.stream()
                .filter(deal -> !alreadyNotifiedHotDealIds.contains(deal.getId()))
                .toList();

            if (!newDealsToNotify.isEmpty()) {

                List<HotDeal> successfullyNotifiedDeals = new ArrayList<>();

                for (HotDeal dealToNotify : newDealsToNotify) {
                    NotificationRecord record = NotificationRecord.builder()
                        .keywordId(subscription.getId())
                        .hotDealId(dealToNotify.getId())
                        .build();

                    try{
                        notificationRecordRepository.save(record);
                        successfullyNotifiedDeals.add(dealToNotify);
                    }catch (DataIntegrityViolationException e) {
                        log.debug("사용자 {} 에게 핫딜 {} (키워드: {}) 알림이 이미 전송됨",
                            subscription.getDiscordUserId(), dealToNotify.getTitle(),
                            subscription.getKeyword());
                    } catch (Exception e) {
                        log.warn("NotificationRecord 저장 중 오류 발생 (KeywordId: {}, HotDealId: {}): {}",
                            subscription.getId(), dealToNotify.getId(), e.getMessage(), e);
                    }
                }
                if (!successfullyNotifiedDeals.isEmpty()) {
                    discordMessageSender.sendDM(subscription.getDiscordUserId(),
                        successfullyNotifiedDeals);
                    log.info("사용자 {} 에게 키워드 '{}'에 대한 {}개의 핫딜 알림 전송 완료",
                        subscription.getDiscordUserId(), subscription.getKeyword(),
                        successfullyNotifiedDeals.size());
                }
            }
        });
    }

}