package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.instance.cache.HSUserCache;
import com.kasp.hstools.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.*;
import java.util.stream.Collectors;

public class CarsCmd implements SlashCommand {
    @Override
    public String getName() {
        return "cars";
    }

    @Override
    public String getDescription() {
        return "check yours or someone else's car garage";
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

        if (user.getCars().isEmpty()) {
            event.replyEmbeds(new Embed(EmbedType.DEFAULT, "No Cars", "**" + user.getIgn() + "** has no cars set up `/setupcars`", 1).build()).setEphemeral(true).queue();
            return;
        }

        Comparator<String> rankComparator = (r1, r2) -> {
            if (r1.equals("SSS") && !r2.equals("SSS")) return -1;
            if (r2.equals("SSS") && !r1.equals("SSS")) return 1;

            if (r1.equals("SS") && !r2.equals("SS")) return -1;
            if (r2.equals("SS") && !r1.equals("SS")) return 1;

            return r2.compareTo(r1);
        };

        String data = user.getCars().entrySet().stream()
                .filter(entry -> !"X".equalsIgnoreCase(entry.getValue()))
                .sorted((e1, e2) -> {
                    int rankCompare = rankComparator.compare(e1.getValue(), e2.getValue());
                    if (rankCompare != 0) return rankCompare;

                    return e1.getKey().compareToIgnoreCase(e2.getKey());
                })
                .map(entry -> {
                    String formattedName = Arrays.stream(entry.getKey().split("_"))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                            .collect(Collectors.joining(" "));
                    String emoji = Emojis.emojis.getOrDefault(entry.getKey(), "🚗");
                    return String.format("%s **%s** %s", emoji, entry.getValue(), formattedName);
                })
                .collect(Collectors.joining(","));

        InteractionHook hook = event.replyEmbeds(new EmbedBuilder().setTitle("loading...").build()).complete();
        Message embedMsg = hook.retrieveOriginal().complete();

        for (int j = 0; j < (double) List.of(data.split(",")).size(); j+=16) {

            Embed reply = new Embed(EmbedType.DEFAULT, user.getIgn() + "'s garage", "", (int) Math.ceil((double) List.of(data.split(",")).size() / 16.0));

            String desc = "";
            for (int i = 0; i < 16; i++) {
                if (i + j < (double) List.of(data.split(",")).size()) {
                    desc += data.split(",")[i+j] + "\n";
                }
            }
            reply.setDescription(desc);

            if (j == 0) {
                embedMsg.editMessageEmbeds(reply.build()).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
            }

            Embed.addPage(embedMsg.getId(), reply);
        }
    }
}
