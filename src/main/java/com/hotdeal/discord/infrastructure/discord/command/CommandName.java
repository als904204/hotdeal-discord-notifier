package com.hotdeal.discord.infrastructure.discord.command;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 지원하는 슬래시 커맨드 이름과 설명을 정의하는 ENUM
 */
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

}
