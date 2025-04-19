package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadCarDataCmd implements SlashCommand {
    @Override
    public String getName() {
        return "loadcardata";
    }

    @Override
    public String getDescription() {
        return "Load the data generated from the website to complete your car garage setup";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "generated_data", "⚠️ WARNING: this replaces all of your car data", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            String[] cars = event.getOption("generated_data").getAsString().split(",");
            HSUser user = HSUserCache.getUser(event.getMember().getId());

            Map<String, String> carsMap = new HashMap<>();
            for (String s : cars) {
                String[] data = s.split("#");
                carsMap.put(data[0], data[1]);
            }

            user.setCars(carsMap);

            event.replyEmbeds(new Embed(EmbedType.SUCCESS, "Successfully updated your car garage", "You can now view it by doing `/cars`", 1).build()).setEphemeral(true).queue();
        } catch (Exception e) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "Something went wrong", "Please make sure youre using the command right, copying the text right, try again, if nothing works ask help from staff", 1).build()).setEphemeral(true).queue();
        }

    }
}
