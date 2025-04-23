package com.hotdeal.discord.domain.hotdeal;

import lombok.Getter;

@Getter
public enum CommunityType {

    PPOMPPU("뽐뿌"),
    FMKOREA("펨코"),
    ALGUMON("알구몬"),
    QZONE("퀘이사존");

    private final String description;

    CommunityType(String description) {
        this.description = description;
    }

}
