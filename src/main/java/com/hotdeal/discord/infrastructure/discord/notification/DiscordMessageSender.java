package com.hotdeal.discord.infrastructure.discord.notification;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.infrastructure.discord.exception.DiscordMessageSendException;
import com.hotdeal.discord.infrastructure.discord.exception.DiscordUserNotFound;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordMessageSender {

    private final JDA jda;

    /**
     * 디스코드 사용자에게 DM 전송
     * TODO : 스레드가 대기 상태에 빠지거나, 콜백 처리용 내부 스레드 풀 고갈될 위험이 있음, timeout 고려
     *
     * @param discordUserId 디스코드 사용자 ID
     * @throws DiscordMessageSendException 메시지 전송 실패 시 발생
     * @throws DiscordUserNotFound         디스코드 유저 없음
     */
    public void sendDM(String discordUserId, List<HotDeal> deals) {

        List<MessageEmbed> buildMsg = buildEmbedMessage(deals);

        try {

            jda.retrieveUserById(discordUserId).queue(user ->
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(buildMsg).queue(
                            success -> log.debug("메시지 전송 성공: {}", discordUserId),
                            error -> {
                                log.error("메시지 전송 실패: {}", error.getMessage(), error);
                            }
                        ),
                    channelError -> log.error("Private 채널 열기 실패: {}", discordUserId, channelError)
                ));
        } catch (ErrorResponseException e) {
            if (e.getErrorResponse() == ErrorResponse.UNKNOWN_USER) {
                log.warn("Discord ID: {} 사용자를 찾을 수 없음.", discordUserId, e);
                throw new DiscordUserNotFound(ErrorCode.DISCORD_USER_NOT_FOUND, e);
            } else {
                log.error("Discord 사용자 조회/준비 중 오류: {}", discordUserId, e);
                throw new DiscordMessageSendException(ErrorCode.DISCORD_MESSAGE_SEND_FAILED, e);
            }
        }
    }

    public List<MessageEmbed> buildEmbedMessage(List<HotDeal> hotDeals) {

        final int MAX_FIELDS = 25;
        List<MessageEmbed> pages = new ArrayList<>();

        for (int start = 0; start < hotDeals.size(); start += MAX_FIELDS) {
            EmbedBuilder eb = new EmbedBuilder()
                .setTitle("🔥 키워드에 맞는 핫딜을 발견했습니다! 🔥")
                .setColor(Color.RED)
                .setTimestamp(Instant.now())
                .setFooter(String.format("페이지 %d/%d",
                    start / MAX_FIELDS + 1,
                    (hotDeals.size() + MAX_FIELDS - 1) / MAX_FIELDS
                ));

            int end = Math.min(start + MAX_FIELDS, hotDeals.size());
            List<HotDeal> sublist = hotDeals.subList(start, end);
            for (HotDeal deal : sublist) {
                eb.addField(
                    deal.getTitle(),
                    String.format("[바로가기](%s)", deal.getPostUrl()),
                    false
                );
            }
            pages.add(eb.build());

        }

        return pages;
    }

}
