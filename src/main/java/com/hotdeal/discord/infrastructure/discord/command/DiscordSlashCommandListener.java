package com.hotdeal.discord.infrastructure.discord.command;

import com.hotdeal.discord.application.keyword.KeywordService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordSlashCommandListener extends ListenerAdapter {

    private static final String KEYWORD = "키워드";
    private final KeywordService keywordService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String commandNameString = event.getName();

        DiscordSlashCommandName.fromString(commandNameString).ifPresentOrElse(
            command -> handleValidCommand(event, command),
            () -> handleInvalidCommand(event, commandNameString)
        );

    }

    private void handleValidCommand(SlashCommandInteractionEvent event, DiscordSlashCommandName command) {
        log.info("Handling command: {}", command);

        String userId = event.getUser().getId();
        String replyMessage;
        OptionMapping keywordOption;

        switch (command) {
            // 키워드 옵션이 필요한 명령어
            case REGISTER:
                keywordOption = event.getOption(KEYWORD);
                if (keywordOption != null) {
                    String inputKeyword = keywordOption.getAsString();
                    keywordService.registerKeyword(inputKeyword, userId);
                    replyMessage = "**" + inputKeyword + "** 키워드가 등록되었습니다!";
                } else {
                    replyMessage = "키워드를 입력해주세요.";
                }
                break;

            // 키워드 옵션이 필요한 명령어
            case DELETE:
                keywordOption = event.getOption(KEYWORD);
                if (keywordOption != null) {
                    String inputKeyword = keywordOption.getAsString();
                    keywordService.deleteKeyword(inputKeyword, userId);
                    replyMessage = "**" + inputKeyword + "** 키워드가 삭제되었습니다!";
                } else {
                    replyMessage = "삭제할 키워드를 입력해주세요.";
                }
                break;

            // 키워드 옵션이 필요없는 명령어
            case LIST:
                List<String> keywords = keywordService.getKeywordListByUserId(userId);
                if (keywords.isEmpty()) {
                    replyMessage = "등록된 키워드가 없습니다.";
                } else {
                    replyMessage = "등록된 키워드 목록\n" + String.join(", ", keywords);
                }
                break;

            // 키워드 옵션이 필요없는 명령어
            case HELP:
                replyMessage = command.generateHelpMessage();
                break;

            default:
                replyMessage = "알 수 없는 명령어입니다.";
        }

         event.reply(replyMessage).queue();
    }

    private void handleInvalidCommand(SlashCommandInteractionEvent event, String commandNameString) {
        log.warn("Unknown or unhandled command received: {}", commandNameString);
        event.reply("잘못된 명령어입니다. '/도움'을 입력해주세요 " + commandNameString)
            .setEphemeral(true)
            .queue();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        List<CommandData> commandList = new ArrayList<>();

        commandList.add(Commands.slash(DiscordSlashCommandName.REGISTER.getCommandName(), DiscordSlashCommandName.REGISTER.getCommandEx())
            .addOption(OptionType.STRING, "키워드", "등록할 키워드를 입력하세요", true));

        commandList.add(Commands.slash(DiscordSlashCommandName.DELETE.getCommandName(), DiscordSlashCommandName.DELETE.getCommandEx())
            .addOption(OptionType.STRING, "키워드", "삭제할 키워드를 입력하세요", true));

        commandList.add(Commands.slash(DiscordSlashCommandName.LIST.getCommandName(), DiscordSlashCommandName.LIST.getCommandEx()));
        commandList.add(Commands.slash(DiscordSlashCommandName.HELP.getCommandName(), DiscordSlashCommandName.HELP.getCommandEx()));

        event.getGuild().updateCommands().addCommands(commandList).queue(
            success -> log.info("Successfully registered slash commands for guild {}", event.getGuild().getName()),
            error -> log.error("Failed to register slash commands for guild {}", event.getGuild().getId(), error)
        );
    }

}
