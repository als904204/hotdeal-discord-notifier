package com.hotdeal.discord.domain.keyword;

import com.hotdeal.discord.domain.time.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
/*
  @UniqueConstraint
 * 키워드를 등록할 때
 * 사용자가 동일한 키워드를 등록함을 방지
 */
@Table(name = "keyword_subscription", uniqueConstraints = {

    @UniqueConstraint(
        name = "uk_discord_user_keyword_subscription)",
        columnNames = {"discord_user_id", "keyword"})
})
public class KeywordSubscription extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    // DiscordUser 엔티티와 연관관계 설정

    @Builder
    public KeywordSubscription(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
}
