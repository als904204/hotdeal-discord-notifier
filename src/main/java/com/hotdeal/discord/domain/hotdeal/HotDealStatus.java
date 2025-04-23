package com.hotdeal.discord.domain.hotdeal;

import lombok.Getter;

@Getter
public enum HotDealStatus {
    ACTIVE("진행중"),
    END("종료됨");

    private final String description;

    HotDealStatus(String description) {
        this.description = description;
    }
}
