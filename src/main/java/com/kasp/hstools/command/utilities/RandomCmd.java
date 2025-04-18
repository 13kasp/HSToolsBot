package com.kasp.hstools.command.utilities;

import com.kasp.hstools.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomCmd implements SlashCommand {
    @Override
    public String getName() {
        return "rnd";
    }

    @Override
    public String getDescription() {
        return "Get random numbers from list";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<String> numbers = new ArrayList<>(Arrays.asList("1,2,3,4,7,8,9,10,11,12,15,16,18,19,20".split(",")));
        Collections.shuffle(numbers);

        event.reply(numbers.get(0) + ", " + numbers.get(1) + ", " + numbers.get(2) + ", " + numbers.get(3)).queue();
    }
}
