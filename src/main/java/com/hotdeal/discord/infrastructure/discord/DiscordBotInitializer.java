package com.hotdeal.discord.infrastructure.discord;

import com.hotdeal.discord.adapter.discord.commend.SlashCommandListener;
import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import com.hotdeal.discord.infrastructure.discord.exception.DiscordBotInitializationException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.stereotype.Component;

/**
 * JDA 인스턴스의 생성, 초기화, 생명주기 관리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordBotInitializer {

    private final DiscordProperties discordProperties;

    private JDA jda;

    @PostConstruct
    public void initialize() {
        log.info("Discord 봇 초기화를 시작합니다...");
        try {
            this.jda = buildAndAwaitJda();
            log.info("Discord 봇 초기화 완료. 로그인 계정: {}", jda.getSelfUser().getAsTag());
        } catch (DiscordBotInitializationException e) {
            log.error("Discord 봇 초기화 실패: {}", e.getMessage(), e);
            // 초기화 실패는 심각한 문제이므로 예외를 다시 던져 애플리케이션 중단
            throw e;
        } catch (Exception e) {
            log.error("Discord 봇 초기화 중 예상치 못한 오류 발생.", e);
            throw new DiscordBotInitializationException(ErrorCode.DISCORD_INITIALIZATION_FAILED, "예상치 못한 초기화 오류", e);
        }
    }

    private JDA buildAndAwaitJda() throws DiscordBotInitializationException {
        try{
            EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MEMBERS
            );
            return JDABuilder.createDefault(discordProperties.getToken())
                .enableIntents(intents)
                .setActivity(Activity.playing("핫딜 알림"))
                .addEventListeners(new SlashCommandListener())
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build()
                .awaitReady();

        }catch (InvalidTokenException e) {
            throw new DiscordBotInitializationException(ErrorCode.DISCORD_LOGIN_FAILED, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DiscordBotInitializationException(ErrorCode.DISCORD_INITIALIZATION_FAILED, "Discord 봇 초기화 대기 중 인터럽트 발생", e);
        } catch (IllegalArgumentException e) {
            throw new DiscordBotInitializationException(ErrorCode.DISCORD_INITIALIZATION_FAILED, "Discord 설정 오류: " + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (jda != null) {
            log.info("Discord 봇을 종료합니다...");
            jda.shutdown();
            try {
                if (!jda.awaitShutdown(5, TimeUnit.SECONDS)) {
                    log.warn("Discord 봇이 5초 내에 정상적으로 종료되지 않았습니다. 강제 종료합니다.");
                    jda.shutdownNow();
                } else {
                    log.info("Discord 봇이 성공적으로 종료되었습니다.");
                }
            } catch (InterruptedException e) {
                log.error("Discord 봇 종료 대기 중 인터럽트 발생. 강제 종료합니다.", e);
                Thread.currentThread().interrupt();
                jda.shutdownNow();
            }
        }
    }

}
