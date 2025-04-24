package com.hotdeal.discord.infrastructure.discord.command;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {

    REGISTER("등록", "핫딜 키워드를 등록합니다."),
    DELETE("삭제", "등록된 핫딜 키워드를 삭제합니다."),
    LIST("목록", "등록된 핫딜 키워드 목록을 조회합니다."),
    HELP("도움", "명령어 목록을 조회합니다.");

    private final String commandName;
    private final String commandEx;

    public static Optional<CommandName> fromString(String text) {
        return Arrays.stream(values())
            .filter(cmd -> cmd.commandName.equalsIgnoreCase(text))
            .findFirst();
    }

    public String generateHelpMessage() {
        return "**사용 가능한 명령어 목록:**\n" +
            Stream.of(CommandName.values())
                .map(cmd -> String.format("`%s` %s", cmd.getUsageExample(), cmd.getCommandEx()))
                .collect(Collectors.joining("\n"));
    }

    public String getUsageExample() {
        return switch (this) {
            case REGISTER -> "/" + commandName + " <등록할 키워드 이름> ";
            case DELETE -> "/" + commandName + " <삭제할 키워드 이름>";
            case LIST -> "/" + commandName + "<등록한 키워드 목록 조회>";
            case HELP -> "/" + commandName;
        };
    }

}
