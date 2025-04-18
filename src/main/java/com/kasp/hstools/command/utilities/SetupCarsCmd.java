package com.kasp.hstools.command.utilities;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetupCarsCmd implements SlashCommand {
    @Override
    public String getName() {
        return "setupcars";
    }

    @Override
    public String getDescription() {
        return "Get the link to set up your garage data (follow the instructions on the website after)";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Embed embed = new Embed(EmbedType.DEFAULT, "Website Link", "# https://hstools.vercel.app/\nfollow the instructions on the website to complete setting up your garage",1);

        if (HSUserCache.getUser(event.getMember().getId()) != null) {
            HSUser user = HSUserCache.getUser(event.getMember().getId());
            if (!user.getCars().isEmpty()) {
                Map<String, String> carsMap = user.getCars();
                List<String> carsList = new ArrayList<>();
                for (Map.Entry<String, String> entry : carsMap.entrySet()) {
                    carsList.add(entry.getKey() + "#" + entry.getValue());
                }

                String carsString = String.join(",", carsList);

                embed.addField("Existing Cars", "I've noticed you already have some cars set up, paste this text in the bottom of the website to modify your existing garage: ```" + carsString + "```", false);
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            }
        }

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
