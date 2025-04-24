package com.hotdeal.discord.infrastructure.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
    /**
     * 슬래시 명령어 실행 메서드
     * @param event 슬래시 명령어 이벤트
     * @return 명령어 실행 결과 메시지
     */
    String execute(SlashCommandInteractionEvent event);

    /**
     * 명령어의 CommandData 반환
     * @return 명령어 설정 정보
     */
    CommandData getCommandData();
}
