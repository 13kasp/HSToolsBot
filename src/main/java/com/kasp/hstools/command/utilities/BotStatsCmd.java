package com.kasp.hstools.command.utilities;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.HSTools;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.cache.HSUserCache;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class BotStatsCmd implements SlashCommand {
    @Override
    public String getName() {
        return "botstats";
    }

    @Override
    public String getDescription() {
        return "View some stats about the bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Embed embed = new Embed(EmbedType.DEFAULT, "Bot's Stats", "", 1);
        embed.addField("Servers", "The bot is in `" + HSTools.jda.getGuilds().size() + "` servers", false);
        embed.addField("Users", "There are `" + HSUserCache.getUsers().size() + "` user registered", false);

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
