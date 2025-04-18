package com.kasp.hstools.command.utilities;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import com.kasp.hstools.utils.Emojis;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SharedCarsCmd implements SlashCommand {
    @Override
    public String getName() {
        return "sharedcars";
    }

    @Override
    public String getDescription() {
        return "compares and finds cars specified users share";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user_1", "User 1", true));
        options.add(new OptionData(OptionType.USER, "user_2", "User 2", true));

        for (int i = 0; i < 8; i++) {
            options.add(new OptionData(OptionType.USER, "user_" + (i + 3), "User " + (i + 3), false));
        }
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<HSUser> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OptionMapping option = event.getOption("user_" + i);
            if (option != null) {
                if (HSUserCache.getUser(option.getAsUser().getId()) == null) {
                    event.replyEmbeds(new Embed(EmbedType.ERROR, "Error", option.getAsUser().getAsTag() + " has not used this bot, therefore i do not know what cars they have\nPlease redo the command without this user", 1).build()).setEphemeral(true).queue();
                    return;
                }
                users.add(HSUserCache.getUser(option.getAsUser().getId()));
            }
        }

        Map<String, String> sharedCars = HSUser.findSharedCars(users);

        if (sharedCars.isEmpty()) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "No Shared Cars",
                            "No shared cars found among the specified users", 1).build())
                    .queue();
        } else {
            StringBuilder sharedList = new StringBuilder();
            StringBuilder userNames = new StringBuilder();
            sharedCars.forEach((carName, rank) ->
                    sharedList.append(Emojis.emojis.get(carName) + " **" + rank + "** " + Arrays.stream(carName.split("_"))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                            .collect(Collectors.joining(" ")) + "\n")
            );

            users.forEach((hsUser -> userNames.append("`" + hsUser.getIgn() + "` ")));
            userNames.append("share these cars:\n");

            event.replyEmbeds(new Embed(EmbedType.DEFAULT, "Shared Cars (" +  sharedCars.size() + ")", userNames + sharedList.toString(), 1).build()).queue();
        }
    }
}
