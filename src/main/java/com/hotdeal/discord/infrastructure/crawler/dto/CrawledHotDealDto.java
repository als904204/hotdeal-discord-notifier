package com.hotdeal.discord.infrastructure.crawler.dto;

import com.hotdeal.discord.domain.hotdeal.HotDealStatus;

public record CrawledHotDealDto(String communityCode, String postId, String title, String url,
                                HotDealStatus status) {}
