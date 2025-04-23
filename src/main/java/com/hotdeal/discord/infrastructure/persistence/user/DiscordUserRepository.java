package com.hotdeal.discord.infrastructure.persistence.user;

import com.hotdeal.discord.domain.user.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long> {

}
