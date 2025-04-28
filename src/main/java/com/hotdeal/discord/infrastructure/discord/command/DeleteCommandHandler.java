package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import com.hotdeal.discord.domain.keyword.exception.KeywordNotFoundException;
import com.hotdeal.discord.infrastructure.discord.config.DiscordProperties;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
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
        var userId = event.getUser().getId();
        var option = event.getOption(properties.getKeyword());

        event.deferReply(true).queue();

        if(option == null) {
            event.getHook()
                .sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("키워드 삭제 실패")
                    .setDescription("❗ 삭제할 키워드를 입력해주세요.")
                    .setColor(Color.RED)
                    .build())
                .queue();
            return;
        }

        var keyword = option.getAsString();

        try {
            keywordService.deleteKeyword(userId, keyword);

            event.getHook()
                .sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("키워드 삭제 완료")
                    .setDescription(String.format("✅ **%s** 키워드가 성공적으로 삭제되었습니다!", keyword))
                    .setColor(Color.GREEN)
                    .build())
                .queue();

        } catch (KeywordNotFoundException e) {

            var currentKeywordList = keywordService.getKeywordListByUserId(userId);

            String listText = currentKeywordList.isEmpty()
                ? "📭 등록된 키워드가 없습니다."
                : currentKeywordList.stream()
                    .map(s -> "• " + s)
                    .collect(Collectors.joining("\n"));

            String desc = String.format(
                "⚠️ **키워드를 찾을 수 없습니다.**\n\n현재 등록된 키워드 목록\n%s\n\n`/삭제 <키워드>` 명령어로 다시 시도하세요.",
                listText
            );

            event.getHook()
                .sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("키워드 삭제 실패")
                    .setDescription(desc)
                    .setColor(Color.ORANGE)
                    .build())
                .queue();

        } catch (Exception e) {

            var eb = new EmbedBuilder()
                .setTitle("오류 발생")
                .setDescription("❌ 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                .setColor(Color.RED)
                .build();

            event.getHook()
                .sendMessageEmbeds(eb)
                .queue();
        }

    }

}
