package com.hotdeal.discord.infrastructure.persistence.notification;

import com.hotdeal.discord.domain.notification.NotificationRecord;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {

    // 특정 keywordId에 대해 주어진 hotDealId 목록 중 이미 존재하는 hotDealId들만 조회
    @Query("SELECT nr.hotDealId FROM NotificationRecord nr WHERE nr.keywordId = :keywordId AND nr.hotDealId IN :hotDealIds")
    Set<Long> NotifiedHotDealIdsByKeywordIdAndHotDealIdsIn(@Param("keywordId") Long keywordId,
        @Param("hotDealIds") List<Long> hotDealIds);

}