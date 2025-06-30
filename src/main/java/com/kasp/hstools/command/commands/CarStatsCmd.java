package com.kasp.hstools.command.commands;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Car;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.cache.CarCache;
import com.kasp.hstools.utils.Emojis;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class CarStatsCmd implements SlashCommand {
    @Override
    public String getName() {
        return "carstats";
    }

    @Override
    public String getDescription() {
        return "View specified car's stats";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "car", "Choose a car", true).setAutoComplete(true),
                new OptionData(OptionType.INTEGER, "level", "Input the car's level (from 1 to 30)", true).setRequiredRange(1, 30)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String carOption = event.getOption("car").getAsString();
        int carLevel = event.getOption("level").getAsInt();

        if (CarCache.getCar(carOption) == null) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "Car not found", "Car `" + carOption + "` not found", 1).build()).setEphemeral(true).queue();
            return;
        }

        Car car = CarCache.getCar(carOption);

        Embed embed = new Embed(EmbedType.DEFAULT, Emojis.emojis.get(carOption) + " " + car.getNameFormatted() + " Info", "Lv. " + carLevel + ", no rank (purple) upgrades, no parts", 1);
        String mainStatsContent = Emojis.emojis.get("stats_earlyacc") + " Early Acc: `" + car.getEarlyAcc(carLevel, false) + "`\n" + Emojis.emojis.get("stats_midacc") + " Mid Acc: `" + car.getMidAcc(carLevel, false) + "`\n" + Emojis.emojis.get("stats_topspeed") + " Top Speed: `" + car.getTopSpeed(carLevel, false) + "`\n" + Emojis.emojis.get("stats_handling") + " Handling: `" + car.getHandling(carLevel, false) + "`";
        embed.addField("Main Stats", mainStatsContent, true);

        String subStatsContent = Emojis.emojis.get("stats_drift") + " Drift: `" + car.getDrift() + "`\n" + Emojis.emojis.get("stats_grip") + " Grip: `" + car.getGrip() + "`\n" + Emojis.emojis.get("stats_offroad") + " Offroad: `" + car.getOffroad() + "`\n" + Emojis.emojis.get("stats_boostdur") + " Boost Dur: `" + car.getBoostDur() + "`\n" + Emojis.emojis.get("stats_booststr") + " Boost Str: `" + car.getBoostStr() + "`";
        embed.addField("Sub Stats", subStatsContent, true);

        String extraStatsContent = "◇ Type: `" + car.getCarType().toString() + "`\n◇ Skin Bonus: `+" + car.formatSkinBonus() + "`";
        embed.addField("Extra Stats", extraStatsContent, true);

        event.replyEmbeds(embed.build()).queue();
    }
}
