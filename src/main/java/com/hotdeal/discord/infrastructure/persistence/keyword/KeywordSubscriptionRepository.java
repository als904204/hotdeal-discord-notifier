package com.hotdeal.discord.infrastructure.persistence.keyword;

import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordSubscriptionRepository extends JpaRepository<KeywordSubscription, Long> {

}
