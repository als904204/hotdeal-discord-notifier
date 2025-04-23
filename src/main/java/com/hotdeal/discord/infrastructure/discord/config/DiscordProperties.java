package com.hotdeal.discord.infrastructure.discord.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "discord.bot")
@Validated
public class DiscordProperties {

    @NotBlank(message = "Discord 봇 토큰은 필수입니다.")
    private String token;

    @NotBlank(message = "Discord 채널 ID는 필수입니다.")
    private String channelId;

    @NotBlank(message = "Discord cron 표현식은 필수입니다.")
    private String cron;

}