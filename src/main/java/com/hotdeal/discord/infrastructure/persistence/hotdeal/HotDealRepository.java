package com.hotdeal.discord.infrastructure.persistence.hotdeal;

import com.hotdeal.discord.domain.hotdeal.HotDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotDealRepository extends JpaRepository<HotDeal, Long> {

}
