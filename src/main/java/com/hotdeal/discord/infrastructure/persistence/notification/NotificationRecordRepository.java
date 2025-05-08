package com.hotdeal.discord.infrastructure.persistence.notification;

import com.hotdeal.discord.domain.notification.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {
}