package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import java.awt.Color;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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

        event.deferReply(true).queue();

        var userId = event.getUser().getId();
        var option = event.getOption(properties.getKeyword());

        if (option != null) {
            var kw = option.getAsString();
            keywordService.registerKeyword(userId, kw);

            var eb = new EmbedBuilder()
                .setTitle("키워드 등록 완료")
                .setDescription(MessageFormat.format("✅ **{0}** 키워드가 등록되었습니다!", kw))
                .setColor(Color.GREEN)
                .build();

            event.getHook()
                .sendMessageEmbeds(eb)
                .queue();

        } else {
            EmbedBuilder eb = new EmbedBuilder()
                .setTitle("입력 오류")
                .setDescription("❗ 키워드를 입력해주세요.")
                .setColor(Color.RED);
            event.getHook()
                .sendMessageEmbeds(eb.build())
                .queue();
        }
    }

}
