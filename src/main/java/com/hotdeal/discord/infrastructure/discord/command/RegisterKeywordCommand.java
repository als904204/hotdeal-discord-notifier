package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterKeywordCommand implements SlashCommand {

    private static final String KEYWORD = "키워드";
    private final KeywordService keywordService;

    @Override
    public String execute(SlashCommandInteractionEvent event) {
        var userId = event.getUser().getId();
        OptionMapping keywordOption = event.getOption(KEYWORD);
        return "";
    }

    @Override
    public CommandData getCommandData() {
        return null;
    }
}
