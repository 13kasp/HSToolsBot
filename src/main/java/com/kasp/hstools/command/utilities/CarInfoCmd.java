package com.kasp.hstools.command.utilities;

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

public class CarInfoCmd implements SlashCommand {
    @Override
    public String getName() {
        return "carinfo";
    }

    @Override
    public String getDescription() {
        return "View info about a certain car";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "car", "Choose a car", true).setAutoComplete(true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String carOption = event.getOption("car").getAsString();

        if (CarCache.getCar(carOption) == null) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "Car not found", "Car `" + carOption + "` not found\nThere's a high chance this car is not set up in this bot yet since this is a new feature", 1).build()).setEphemeral(true).queue();
            return;
        }

        Car car = CarCache.getCar(carOption);

        Embed embed = new Embed(EmbedType.DEFAULT, Emojis.emojis.get(carOption) + " " + car.getName().substring(0, 1).toUpperCase() + car.getName().substring(1) + " Info", "Lv. 1, no rank (purple) upgrades", 1);
        String mainStatsContent = "◇ Early Acc: `" + car.getEarlyAcc() + "`\n◇ Mid Acc: `" + car.getMidAcc() + "`\n◇ Top Speed: `" + car.getTopSpeed() + "`\n◇ Handling: `" + car.getHandling() + "`";
        embed.addField("Main Stats", mainStatsContent, true);

        String subStatsContent = "◇ Drift: `" + car.getDrift() + "`\n◇ Grip: `" + car.getGrip() + "`\n◇ Offroad: `" + car.getOffroad() + "`\n◇ Boost Dur: `" + car.getBoostDur() + "`\n◇ Boost Str: `" + car.getBoostStr() + "`";
        embed.addField("Sub Stats", subStatsContent, true);

        String extraStatsContent = "◇ Type: `" + car.getCarType().toString() + "`\n◇ Skin Bonus: `+" + car.formatSkinBonus() + "`";
        embed.addField("Extra Stats", extraStatsContent, true);

        event.replyEmbeds(embed.build()).queue();
    }
}
