package com.hotdeal.discord.infrastructure.discord.command;

import java.awt.Color;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {

    @Override
    public void handle(SlashCommandInteractionEvent event) {

        EmbedBuilder eb = new EmbedBuilder()
            .setTitle("🛠️ 사용 가능한 명령어 목록")
            .setColor(Color.CYAN);

        for(CommandName cmd : CommandName.values()) {

            var usage = switch (cmd) {
                case REGISTER, DELETE -> "/" + cmd.getCommandName() + " <키워드>";
                case LIST, HELP       -> "/" + cmd.getCommandName();
            };

            eb.addField(usage, cmd.getCommandEx(), false);
        }

        event.replyEmbeds(eb.build())
            .setEphemeral(true)
            .queue();

    }

}
