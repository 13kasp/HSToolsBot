package com.kasp.hstools.command.commands;

import com.kasp.hstools.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Random;

public class Random4DigitCmd implements SlashCommand {
    @Override
    public String getName() {
        return "rnd4";
    }

    @Override
    public String getDescription() {
        return "Generates a random 4 digit number (used for tourneys ignore ts)";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        event.reply(String.valueOf(randomNumber)).queue();
    }
}
