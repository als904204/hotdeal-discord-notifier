package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

/**
 * '/목록' 명령어 처리: 사용자 키워드 목록을 조회하여 응답한다.
 */
@Component
@RequiredArgsConstructor
public class ListCommandHandler implements CommandHandler {

    private final KeywordService keywordService;

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        List<String> keywords = keywordService.getKeywordListByUserId(userId);
        String replyMessage;

        if (keywords.isEmpty()) {
            replyMessage = "등록된 키워드가 없습니다.";
        } else {
            replyMessage = "등록된 키워드 목록:\n" + String.join(", ", keywords);
        }

        event.reply(replyMessage).queue();
    }

}
