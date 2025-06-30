package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class LiveriesCmd implements SlashCommand {
    @Override
    public String getName() {
        return "liveries";
    }

    @Override
    public String getDescription() {
        return "Get the link to browse all community liveries easily";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new Embed(EmbedType.DEFAULT, "Community Liveries", "*NOTE: All liveries are automatically loaded from the [HotSlide Discord Server](https://discord.gg/UcYUBNtyMU)*\n# https://hstools.vercel.app/liveries", 1).build()).setEphemeral(true).queue();
    }
}
