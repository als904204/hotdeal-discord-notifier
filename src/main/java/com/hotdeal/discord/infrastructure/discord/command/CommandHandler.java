package com.hotdeal.discord.infrastructure.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandHandler {
    void handle(SlashCommandInteractionEvent event);
}
