package com.hotdeal.discord.infrastructure.discord.notification;

import com.hotdeal.discord.application.hotdeal.HotDealService;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import com.hotdeal.discord.infrastructure.persistence.keyword.KeywordSubscriptionRepository;
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
    private final DiscordMessageSender discordMessageSender;

    public void processKeywordMatchNotifications() {

        // TODO : 현재는 모두 조회임 추후 개선
        List<HotDeal> activeHotDeals = hotDealService.findActiveHotDeals();

        if (activeHotDeals.isEmpty()) {
            log.info("활성 상태인 핫딜이 없습니다. 디스코드 알림 종료");
            return;
        }

        log.info("활성 핫딜 {}개 발견, 알림 처리 시작", activeHotDeals.size());

        // TODO : 현재는 모두 조회임 추후 개선, 이미 전송된 알람도 중복 전송중
        List<KeywordSubscription> allSubscriptions = keywordSubscriptionRepository.findAll();

        // TODO : 키워드 매칭 로직 개선 필요, 현재 모든 구독자에 대해 동일한 핫딜 목록을 반복적으로 필터링중
        for (KeywordSubscription subscription : allSubscriptions) {

            // 키워드에 맞는 핫딜 필터링
            List<HotDeal> matchingDeals = hotDealService.filterMatchingHotDeals(activeHotDeals,
                subscription.getKeyword());

            if (!matchingDeals.isEmpty()) {
                String message = hotDealService.buildHotDealMessage(matchingDeals);
                discordMessageSender.sendDM(subscription.getDiscordUserId(), message);
                log.info("사용자 ID: {}에게 {}개의 핫딜 알림 전송 완료", subscription.getDiscordUserId(),
                    matchingDeals.size());
            }
        }
    }


}
