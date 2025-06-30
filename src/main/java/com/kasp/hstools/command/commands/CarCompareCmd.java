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

public class CarCompareCmd implements SlashCommand {
    @Override
    public String getName() {
        return "carcompare";
    }

    @Override
    public String getDescription() {
        return "Compare 2 cars";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "car1", "Choose car 1", true).setAutoComplete(true),
                new OptionData(OptionType.STRING, "car2", "Choose car 2", true).setAutoComplete(true),
                new OptionData(OptionType.INTEGER, "level", "Input the car's level (from 1 to 30)", true).setRequiredRange(1, 30),
                new OptionData(OptionType.BOOLEAN, "skinbonus", "Take skin bonus into consideration?", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Car car1 = CarCache.getCar(event.getOption("car1").getAsString());
        Car car2 = CarCache.getCar(event.getOption("car2").getAsString());
        int level = event.getOption("level").getAsInt();
        boolean skinBonus = event.getOption("skinbonus").getAsBoolean();

        if (car1 == null) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "Car not found", "Car `" + event.getOption("car1").getAsString() + "` not found", 1).build()).setEphemeral(true).queue();
            return;
        }

        if (car2 == null) {
            event.replyEmbeds(new Embed(EmbedType.ERROR, "Car not found", "Car `" + event.getOption("car2").getAsString() + "` not found", 1).build()).setEphemeral(true).queue();
            return;
        }

        Embed embed = new Embed(EmbedType.DEFAULT, "Car Comparison", "Lv. `" + level + "` | Skin bonus: `" + skinBonus + "`", 1);

        String mainStatsContent1 = Emojis.emojis.get("stats_earlyacc") + " Early Acc: `" + car1.getEarlyAcc(level, skinBonus) + "`" + (car1.getEarlyAcc(level, skinBonus) == car2.getEarlyAcc(level, skinBonus) ? "" : (car1.getEarlyAcc(level, skinBonus) > car2.getEarlyAcc(level, skinBonus) ? Emojis.emojis.get("upgrade") : Emojis.emojis.get("downgrade"))) +
                "\n" + Emojis.emojis.get("stats_midacc") + " Mid Acc: `" + car1.getMidAcc(level, skinBonus) + "`" + (car1.getMidAcc(level, skinBonus) == car2.getMidAcc(level, skinBonus) ? "" : (car1.getMidAcc(level, skinBonus) > car2.getMidAcc(level, skinBonus) ? Emojis.emojis.get("upgrade") : Emojis.emojis.get("downgrade"))) +
                "\n" + Emojis.emojis.get("stats_topspeed") + " Top Speed: `" + car1.getTopSpeed(level, skinBonus) + "`" + (car1.getTopSpeed(level, skinBonus) == car2.getTopSpeed(level, skinBonus) ? "" : (car1.getTopSpeed(level, skinBonus) > car2.getTopSpeed(level, skinBonus) ? Emojis.emojis.get("upgrade") : Emojis.emojis.get("downgrade"))) +
                "\n" + Emojis.emojis.get("stats_handling") + " Handling: `" + car1.getHandling(level, skinBonus) + "`" + (car1.getHandling(level, skinBonus) == car2.getHandling(level, skinBonus) ? "" : (car1.getHandling(level, skinBonus) > car2.getHandling(level, skinBonus) ? Emojis.emojis.get("upgrade") : Emojis.emojis.get("downgrade")));

        String mainStatsContent2 = Emojis.emojis.get("stats_earlyacc") + " Early Acc: `" + car2.getEarlyAcc(level, skinBonus) +
                "`\n" + Emojis.emojis.get("stats_midacc") + " Mid Acc: `" + car2.getMidAcc(level, skinBonus) +
                "`\n" + Emojis.emojis.get("stats_topspeed") + " Top Speed: `" + car2.getTopSpeed(level, skinBonus) +
                "`\n" + Emojis.emojis.get("stats_handling") + " Handling: `" + car2.getHandling(level, skinBonus) + "`";

        embed.addField(Emojis.emojis.get(car1.getName()) + " " + car1.getNameFormatted(), mainStatsContent1, true);
        embed.addField(Emojis.emojis.get(car2.getName()) + " " + car2.getNameFormatted(), mainStatsContent2, true);

        event.replyEmbeds(embed.build()).queue();
    }
}
