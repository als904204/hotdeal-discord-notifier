package com.hotdeal.discord.infrastructure.discord.command;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String helpMessage = CommandName.HELP.generateHelpMessage();
        event.reply(helpMessage).queue();
    }

}
