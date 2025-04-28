package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

/**
 * '/삭제' 명령어 처리: 사용자 키워드를 삭제하고 결과를 응답한다
 */
@Component
@RequiredArgsConstructor
public class DeleteCommandHandler implements CommandHandler {

    private final DiscordProperties properties;
    private final KeywordService keywordService;

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        var option = event.getOption(properties.getKeyword());
        String replyMessage;

        if (Objects.nonNull(option)) {
            String keyword = option.getAsString();
            keywordService.deleteKeyword(userId, keyword);
            replyMessage = "**" + keyword + "** 키워드가 삭제되었습니다!";
        } else {
            replyMessage = "삭제할 키워드를 입력해주세요.";
        }

        event.reply(replyMessage).queue();
    }

}
