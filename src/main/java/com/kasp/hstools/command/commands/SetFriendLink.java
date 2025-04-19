package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SetFriendLink implements SlashCommand {
    @Override
    public String getName() {
        return "setfriendlink";
    }

    @Override
    public String getDescription() {
        return "Set your friend link to be displayed in /profile. Set it as \"-\" to disable it from showing up";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "link", "Your Hot Slide friend link (or \"-\" if you do not want to share it)", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        HSUser user = HSUserCache.getUser(event.getMember().getId());

        user.setFriendLink(event.getOption("link").getAsString());

        event.replyEmbeds(new Embed(EmbedType.SUCCESS, "Friend Link Set Successfully", "Changed your friend link to `" + event.getOption("link").getAsString() + "`\n\n*You can check if it's correct by doing /profile*", 1).build()).setEphemeral(true).queue();
    }
}
