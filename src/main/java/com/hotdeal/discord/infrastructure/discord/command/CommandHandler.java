package com.hotdeal.discord.infrastructure.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * 특정 슬래시 커맨드를 처리하기 위한 핸들러 인터페이스
 */
public interface CommandHandler {
    void handle(SlashCommandInteractionEvent event);
}
