package com.hotdeal.discord.infrastructure.discord.notification;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.infrastructure.discord.exception.DiscordMessageSendException;
import com.hotdeal.discord.infrastructure.discord.exception.DiscordUserNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
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
     *
     * @param discordUserId 디스코드 사용자 ID
     * @param message 전송할 메시지 내용
     * @throws DiscordMessageSendException 메시지 전송 실패 시 발생
     * @throws DiscordUserNotFound 디스코드 유저 없음
     */
    public void sendDM(String discordUserId, String message) {
        try {
            User user = jda.retrieveUserById(discordUserId).complete();

            user.openPrivateChannel().queue(channel ->
                channel.sendMessage(message).queue(
                    success -> log.debug("메시지 전송 성공: {}", discordUserId),
                    error -> {
                        log.error("메시지 전송 실패: {}", error.getMessage(), error);
                    }
                ),
                channelError -> log.error("Private 채널 열기 실패: {}", discordUserId, channelError)
            );
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

}
