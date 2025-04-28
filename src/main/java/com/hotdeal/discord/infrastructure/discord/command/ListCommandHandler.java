package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
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

        var userId = event.getUser().getId();
        var keywords = keywordService.getKeywordListByUserId(userId);

        var eb = new EmbedBuilder()
            .setColor(Color.BLUE);

        if (keywords.isEmpty()) {
            eb.setTitle("키워드 목록 조회")
                .setDescription("📭 등록된 키워드가 없습니다.");
        } else {

            AtomicInteger index = new AtomicInteger(1);

            var kwList = keywords.stream()
                .map(keyword -> index.getAndIncrement() + ". " + keyword)
                .collect(Collectors.joining("\n"));;

            var description = String.format(
                "📋 등록된 키워드 목록\n```text\n%s\n```\n총 %d개 등록됨 ✅",
                kwList,
                keywords.size()
            );

            eb.setTitle("키워드 목록 조회")
                .setDescription(description);
        }

        event.replyEmbeds(eb.build())
            .setEphemeral(true)
            .queue();
    }

}
