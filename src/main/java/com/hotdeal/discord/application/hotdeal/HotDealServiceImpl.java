package com.hotdeal.discord.application.hotdeal;

import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.hotdeal.HotDealStatus;
import com.hotdeal.discord.infrastructure.persistence.hotdeal.HotDealRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotDealServiceImpl implements HotDealService{

    private final HotDealRepository hotDealRepository;
    private static final long EXPIRATION_HOURS = 6; // 만료 시간 (6시간)

    @Override
    public List<HotDeal> findActiveHotDeals() {
        return hotDealRepository.findByStatus(HotDealStatus.ACTIVE);
    }

    @Override
    public List<HotDeal> filterMatchingHotDeals(List<HotDeal> hotDeals, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return List.of();
        }

        String lowercaseKeyword = keyword.toLowerCase();

        return hotDeals.stream()
            .filter(hotDeal -> hotDeal.getTitle().toLowerCase().contains(lowercaseKeyword))
            .toList();
    }

    @Override
    public String buildHotDealMessage(List<HotDeal> hotDeals) {

        StringBuilder messageBuilder = new StringBuilder("🔥 키워드에 맞는 핫딜을 발견했습니다! 🔥\n\n");

        for (HotDeal deal : hotDeals) {
            messageBuilder.append("- ")
                .append(deal.getTitle())
                .append("\n")
                .append(deal.getPostUrl())
                .append("\n\n");
        }

        return messageBuilder.toString();
    }

    @Transactional
    @Override
    public void expireOldHotDeals() {
        List<HotDeal> activeDeals = hotDealRepository.findByStatus(HotDealStatus.ACTIVE);
        LocalDateTime sixHoursAgo = LocalDateTime.now().minusHours(EXPIRATION_HOURS);
        int cnt = 0;
        for (HotDeal deal : activeDeals) {
            if (deal.getCreatedAt().isBefore(sixHoursAgo)) {
                deal.updateStatusIfEnded(HotDealStatus.END);
                ++cnt;
            }
        }
        log.info("{}건, 핫딜 상태 변경 완료", cnt);
    }

}
