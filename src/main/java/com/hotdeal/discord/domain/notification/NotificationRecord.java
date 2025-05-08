package com.hotdeal.discord.domain.notification;

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
@Table(name = "notification_records", uniqueConstraints = {
    // 키워드 ID와 핫딜 ID 조합의 유니크 제약조건 -> 중복 알림 방지
    @UniqueConstraint(
        columnNames = {"keyword_id", "hot_deal_id"},
        name = "uk_keyword_deal_notification"
    )
})
public class NotificationRecord extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Column(name = "hot_deal_id", nullable = false)
    private Long hotDealId;

    @Builder
    public NotificationRecord(Long keywordId, Long hotDealId) {
        this.keywordId = keywordId;
        this.hotDealId = hotDealId;
    }
}