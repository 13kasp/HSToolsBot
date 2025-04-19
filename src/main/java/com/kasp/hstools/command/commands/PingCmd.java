package com.kasp.hstools.command.commands;

import com.kasp.hstools.HSTools;
import com.kasp.hstools.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class PingCmd implements SlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Check bot's latency";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long startTime = System.currentTimeMillis();
        long apiPing = HSTools.jda.getGatewayPing();

        event.reply("Testing...").setEphemeral(true)
                .flatMap(v -> {
                    long processingTime = System.currentTimeMillis() - startTime;
                    return event.getHook().editOriginalFormat(
                            "## Latency Results:\n" +
                                    "- Discord API: `%d ms`\n" +
                                    "- Processing: `%d ms`\n" +
                                    "- Total: `%d ms`",
                            apiPing,
                            processingTime - apiPing,
                            processingTime
                    );
                })
                .queue();
    }
}
