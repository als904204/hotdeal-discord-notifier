package com.hotdeal.discord.infrastructure.discord.command;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 각 CommandName에 대응하는 CommandHandler 빈을 구성하는 설정 클래스
 */
@Configuration
public class CommandHandlerConfig {

    @Bean
    public Map<CommandName, CommandHandler> commandHandlerMap(
        RegisterCommandHandler registerHandler,
        DeleteCommandHandler deleteHandler,
        ListCommandHandler listHandler,
        HelpCommandHandler helpHandler
    ) {
        return Map.of(
            CommandName.REGISTER, registerHandler,
            CommandName.DELETE, deleteHandler,
            CommandName.LIST, listHandler,
            CommandName.HELP, helpHandler
        );
    }

}
