package com.hotdeal.discord.infrastructure.persistence.hotdeal;

import com.hotdeal.discord.domain.hotdeal.CommunityType;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.hotdeal.HotDealStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotDealRepository extends JpaRepository<HotDeal, Long> {

    Optional<HotDeal> findByCommunityCodeAndPostId(CommunityType communityCode, String postId);

    List<HotDeal> findByStatus(HotDealStatus status);
}
