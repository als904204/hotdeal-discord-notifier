package com.hotdeal.discord.infrastructure.persistence.keyword;

import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordSubscriptionRepository extends JpaRepository<KeywordSubscription, Long> {

    Optional<KeywordSubscription> findByDiscordUserIdAndKeyword(String userId, String keyword);

    List<KeywordSubscription> findAllByDiscordUserId(String userId);
}
