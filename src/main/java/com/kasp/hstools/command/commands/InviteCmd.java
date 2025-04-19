package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class InviteCmd implements SlashCommand {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Get the link to invite this bot to your own server";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new Embed(EmbedType.DEFAULT, "Invite Link", "*NOTE: All data is shared across servers*\n\nhttps://discord.com/oauth2/authorize?client_id=1362136845273534524&permissions=8&integration_type=0&scope=applications.commands+bot", 1).build()).setEphemeral(true).queue();
    }
}
