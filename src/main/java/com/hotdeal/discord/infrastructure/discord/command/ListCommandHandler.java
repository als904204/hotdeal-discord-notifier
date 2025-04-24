package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

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
