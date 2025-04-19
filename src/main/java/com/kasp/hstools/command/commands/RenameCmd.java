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

public class RenameCmd implements SlashCommand {

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getDescription() {
        return "Change your /profile name. You should probably change it to whatever you have in-game";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "name", "new name", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        HSUser user = HSUserCache.getUser(event.getMember().getId());

        user.setIgn(event.getOption("name").getAsString());

        event.replyEmbeds(new Embed(EmbedType.SUCCESS, "Renamed Successfully", "Changed your name to `" + event.getOption("name").getAsString() + "`\n\n*This is not your discord name or server nickname, its just the name that will be displayed in the bot's commands like /profile*", 1).build()).setEphemeral(true).queue();
    }
}
