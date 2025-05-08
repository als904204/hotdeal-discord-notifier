package com.hotdeal.discord.application.hotdeal;

import com.hotdeal.discord.domain.hotdeal.HotDeal;
import java.util.List;

public interface HotDealService {

    List<HotDeal> findActiveHotDeals();

    List<HotDeal> filterMatchingHotDeals(List<HotDeal> hotDeals, String keyword);

    String buildHotDealMessage(List<HotDeal> hotDeals);

    void expireOldHotDeals();
}
