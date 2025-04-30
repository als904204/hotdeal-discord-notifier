package com.hotdeal.discord.infrastructure.persistence.notification;

import com.hotdeal.discord.domain.notification.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {

    /**
     * 특정 키워드와 핫딜에 대한 알림 기록이 이미 존재하는지 확인
     */
    boolean existsByKeywordIdAndHotDealId(Long keywordId, Long hotDealId);

}