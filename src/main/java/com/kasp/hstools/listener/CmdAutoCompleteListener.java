package com.kasp.hstools.listener;

import com.kasp.hstools.instance.cache.CarCache;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

public class CmdAutoCompleteListener extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if ((event.getName().equals("carstats") && event.getFocusedOption().getName().equals("car")) || (event.getName().equals("carcompare") && (event.getFocusedOption().getName().equals("car1") || event.getFocusedOption().getName().equals("car2")))) {
            String userInput = event.getFocusedOption().getValue().toLowerCase();

            List<Command.Choice> choices = CarCache.getCars().keySet().stream()
                    .filter(car -> car.startsWith(userInput))
                    .limit(25)
                    .map(car -> new Command.Choice(car, car))
                    .collect(Collectors.toList());

            event.replyChoices(choices).queue();
        }
    }
}
