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
import java.util.Map;

public class ProfileCmd implements SlashCommand {
    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "View yours or someone's profile (and stats) related to Hot Slide";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "user", "mention or discord ID of a who's garage you'd like to check. Leave empty to check your own", false)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        HSUser user;

        if (event.getOption("user") != null) {
            if (HSUserCache.getUser(event.getOption("user").getAsString().replaceAll("[^0-9]", "")) == null) {
                event.replyEmbeds(new Embed(EmbedType.ERROR, "Error", "This user does not exist (or hasn't used this bot)", 1).build()).setEphemeral(true).queue();
                return;
            }

            user = HSUserCache.getUser(event.getOption("user").getAsString().replaceAll("[^0-9]", ""));
        }
        else {
            user = HSUserCache.getUser(event.getMember().getId());
        }

        Embed embed = new Embed(EmbedType.DEFAULT, user.getIgn(), "", 1);
        embed.setThumbnailURL(event.getGuild().getMemberById(user.getID()).getEffectiveAvatarUrl());

        Map<String, String> cars = user.getCars();

        long sssCount = cars.values().stream().filter(rank -> rank.equalsIgnoreCase("SSS")).count();
        long ssCount = cars.values().stream().filter(rank -> rank.equalsIgnoreCase("SS")).count();
        long sCount = cars.values().stream().filter(rank -> rank.equalsIgnoreCase("S")).count();

        long totalOwned = cars.values().stream()
                .filter(rank -> !rank.equalsIgnoreCase("X"))
                .count();

        String carsFieldValue = String.format(
                "`%d` owned | `%d` SSS | `%d` SS | `%d` S",
                totalOwned,
                sssCount,
                ssCount,
                sCount
        );

        embed.addField("Cars", carsFieldValue, false);

        if (user.getFriendLink() != null && !user.getFriendLink().trim().isEmpty() && !user.getFriendLink().trim().equals("-")) {
            embed.addField("Friend Link", "[" + user.getIgn() + "'s friend link (click here)" + "](" + user.getFriendLink() + ")", false);
        }

        event.replyEmbeds(embed.build()).queue();
    }
}
