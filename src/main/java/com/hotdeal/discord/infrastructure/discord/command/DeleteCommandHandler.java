package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

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
            keywordService.deleteKeyword(keyword, userId);
            replyMessage = "**" + keyword + "** 키워드가 삭제되었습니다!";
        } else {
            replyMessage = "삭제할 키워드를 입력해주세요.";
        }

        event.reply(replyMessage).queue();
    }

}
