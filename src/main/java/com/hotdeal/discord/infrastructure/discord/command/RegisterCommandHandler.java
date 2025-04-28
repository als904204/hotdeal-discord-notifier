package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

/**
 * '/등록' 명령어 처리: 새로운 키워드를 등록하고 결과를 응답한다.
 */
@Component
@RequiredArgsConstructor
public class RegisterCommandHandler implements CommandHandler {

    private final DiscordProperties properties;
    private final KeywordService keywordService;

    @Override
    public void handle(SlashCommandInteractionEvent event) {

        String userId = event.getUser().getId();
        OptionMapping option = event.getOption(properties.getKeyword());
        String reply;

        if (option != null) {
            String kw = option.getAsString();
            keywordService.registerKeyword(userId, kw);
            reply = "**" + kw + "** 키워드가 등록되었습니다!";
        } else {
            reply = "키워드를 입력해주세요.";
        }
        event.reply(reply).queue();
    }

}
