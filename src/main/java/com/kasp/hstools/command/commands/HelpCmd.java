package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class HelpCmd implements SlashCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "View all useful information related to this bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Embed embed = new Embed(EmbedType.DEFAULT, "What type of help do you need?", "Select one of the drop-down options below for more information about the displayed topic", 1);
        event.replyEmbeds(embed.build())
                .addActionRow(
                        StringSelectMenu.create("choose-help-topic")
                                .addOption("\uD83E\uDD16 All commands", "all-cmds", "View all HSTools commands and their descriptions")
                                .addOption("\uD83D\uDE97 Car garage setup", "car-setup", "A guide on how to set up your car garage (/cars)")
                                .addOption("\uD83D\uDC64 Profile setup", "profile-setup", "A guide on how to set up your profile (/profile)")
                                .build())
                .setEphemeral(true).queue();
    }
}
