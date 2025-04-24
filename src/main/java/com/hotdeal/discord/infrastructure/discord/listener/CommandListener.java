package com.hotdeal.discord.infrastructure.discord.listener;

import com.hotdeal.discord.infrastructure.discord.command.CommandHandler;
import com.hotdeal.discord.infrastructure.discord.command.CommandName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {

    private final Map<CommandName, CommandHandler> handlers;

    /**
     * Discord 채널에서 전달된 슬래시 명령어를 처리합니다.
     * 명령어 이름을 기반으로 적절한 핸들러를 찾아 위임하고,
     * 일치하는 핸들러가 없을 경우 오류 메시지를 반환합니다.
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandName.fromString(event.getName())
            .map(handlers::get)
            .ifPresentOrElse(
                handler -> handler.handle(event),
                () -> {
                    log.warn("명령어를 받았지만 에러가 발생하였습니다. 명령어={}", event.getName());
                    event.reply("잘못된 명령어입니다. '/도움'을 입력해주세요.")
                        .setEphemeral(true)
                        .queue();
                }
            );
    }

    /**
     * 채널에서 사용될 슬래시 명령어들을 등록합니다.
     *
     * @param event 채널 준비 이벤트를 나타내는 {@link GuildReadyEvent}.
     */
    @Override
    public void onGuildReady(GuildReadyEvent event) {

        List<CommandData> commandList = new ArrayList<>();

        commandList.add(Commands.slash(CommandName.REGISTER.getCommandName(), CommandName.REGISTER.getCommandEx())
            .addOption(OptionType.STRING, "키워드", "등록할 키워드를 입력하세요", true));

        commandList.add(Commands.slash(CommandName.DELETE.getCommandName(), CommandName.DELETE.getCommandEx())
            .addOption(OptionType.STRING, "키워드", "삭제할 키워드를 입력하세요", true));

        commandList.add(Commands.slash(CommandName.LIST.getCommandName(), CommandName.LIST.getCommandEx()));
        commandList.add(Commands.slash(CommandName.HELP.getCommandName(), CommandName.HELP.getCommandEx()));

        event.getGuild().updateCommands().addCommands(commandList).queue(
            success -> log.info("Successfully registered slash commands for guild {}", event.getGuild().getName()),
            error -> log.error("Failed to register slash commands for guild {}", event.getGuild().getId(), error)
        );
    }

}