package com.kasp.hstools.command.commands;

import com.kasp.hstools.CarType;
import com.kasp.hstools.EmbedType;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Car;
import com.kasp.hstools.instance.Embed;
import com.kasp.hstools.instance.cache.CarCache;
import com.kasp.hstools.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CarsLBCmd implements SlashCommand {
    @Override
    public String getName() {
        return "carslb";
    }

    @Override
    public String getDescription() {
        return "View cars leaderboard ranked by specified statistic";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "category", "Which category of cars would you like to include in the LB", true)
                        .addChoice("All", "all").addChoice("Street", "street").addChoice("Super", "super").addChoice("SUV", "suv").addChoice("Muscle", "muscle"),
                new OptionData(OptionType.INTEGER, "level", "Level of the cars compared (1-30)", true).setRequiredRange(1, 30),
                new OptionData(OptionType.STRING, "statistic", "What statistic to rank the cars by", true)
                        .addChoice("Early Acc", "earlyacc").addChoice("Mid Acc", "midacc").addChoice("Top Speed", "topspeed").addChoice("Handling", "handling"),
                new OptionData(OptionType.BOOLEAN, "skinbonus", "Take skin bonus into consideration?", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String category = event.getOption("category").getAsString();
        int level = event.getOption("level").getAsInt();
        String stat = event.getOption("statistic").getAsString();
        boolean skinBonus = event.getOption("skinbonus").getAsBoolean();

        List<Car> cars = new ArrayList<>(CarCache.getCars().values());
        if (!category.equals("all")) {
            CarType selectedType = CarType.valueOf(category.toUpperCase());
            cars.removeIf(car -> car.getCarType() != selectedType);
        }

        Comparator<Car> comparator = switch (stat) {
            case "earlyacc" -> Comparator.comparingInt(c -> c.getEarlyAcc(level, skinBonus));
            case "midacc" -> Comparator.comparingInt(c -> c.getMidAcc(level, skinBonus));
            case "topspeed" -> Comparator.comparingInt(c -> c.getTopSpeed(level, skinBonus));
            case "handling" -> Comparator.comparingInt(c -> c.getHandling(level, skinBonus));
            default -> Comparator.comparingInt(c -> c.getMidAcc(level, skinBonus));
        };

        cars.sort(comparator.reversed());

        InteractionHook hook = event.replyEmbeds(new EmbedBuilder().setTitle("loading...").build()).complete();
        Message embedMsg = hook.retrieveOriginal().complete();

        for (int j = 0; j < cars.size(); j+=16) {

            Embed reply = new Embed(EmbedType.DEFAULT, capitalize(category) + " Cars Leaderboard By " + capitalize(stat), "", (int) Math.ceil(cars.size() / 16.0));

            String desc = "Lv. `" + level + "` | Skin bonus: `" + skinBonus + "`\n\n";
            for (int i = 0; i < 16; i++) {
                if (i + j < cars.size()) {
                    StringBuilder sb = new StringBuilder();
                    Car car = cars.get(j + i);
                    int value = switch (stat) {
                        case "earlyacc" -> car.getEarlyAcc(level, skinBonus);
                        case "midacc" -> car.getMidAcc(level, skinBonus);
                        case "topspeed" -> car.getTopSpeed(level, skinBonus);
                        case "handling" -> car.getHandling(level, skinBonus);
                        default -> 0;
                    };
                    sb.append("**#").append(j + i + 1).append("** ").append(Emojis.emojis.get(car.getName())).append(" `")
                            .append(car.getNameFormatted()).append("` - ").append(value);

                    desc += sb + "\n";
                }
            }
            reply.setDescription(desc);

            if (j == 0) {
                embedMsg.editMessageEmbeds(reply.build()).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
            }

            Embed.addPage(embedMsg.getId(), reply);
        }
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
