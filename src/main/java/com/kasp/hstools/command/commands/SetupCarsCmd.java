package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        String desc = "## https://hstools.vercel.app/carsetup/\nfollow the instructions on the website to complete setting up your garage";
        Embed embed = new Embed(EmbedType.DEFAULT, "Website Link", desc,1);

        if (HSUserCache.getUser(event.getMember().getId()) != null) {
            HSUser user = HSUserCache.getUser(event.getMember().getId());
            if (!user.getCars().isEmpty()) {
                Map<String, String> carsMap = user.getCars();
                List<String> carsList = new ArrayList<>();
                for (Map.Entry<String, String> entry : carsMap.entrySet()) {
                    carsList.add(entry.getKey() + "#" + entry.getValue());
                }

                String carsString = String.join(",", carsList);
                String encodedCars = URLEncoder.encode(carsString, StandardCharsets.UTF_8);

                desc += "\n\n**Existing Cars**\nI've noticed you already have some cars set up\n[Click Here to edit your existing garage](https://hstools.vercel.app/carsetup?data=" + encodedCars + ")";
                embed.setDescription(desc);

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            }
        }

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
