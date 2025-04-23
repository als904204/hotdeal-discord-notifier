package com.hotdeal.discord.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "discord_user")
public class DiscordUser {

    // Discord User 고유 ID (String)를 기본 키로 사용
    @Id
    @Column(name = "discord_user_id")
    private String id;

    @Column(name = "username", nullable = true)
    private String username;

    @Builder
    public DiscordUser(String id, String username, String globalName) {
        this.id = id;
        this.username = username;
    }

}
