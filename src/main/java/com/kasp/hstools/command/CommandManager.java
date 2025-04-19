package com.kasp.hstools.command;

import com.kasp.hstools.HSTools;
import com.kasp.hstools.command.commands.*;
import com.kasp.hstools.database.SQLUserManager;
import com.kasp.hstools.instance.HSUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {
    private final Map<String, SlashCommand> commands = new HashMap<>();

    public CommandManager() {
        registerCommand(new PingCmd());
        registerCommand(new RandomCmd());
        registerCommand(new Random4DigitCmd());
        registerCommand(new SetupCarsCmd());
        registerCommand(new LoadCarDataCmd());
        registerCommand(new CarsCmd());
        registerCommand(new SharedCarsCmd());
        registerCommand(new CarStatsCmd());
        registerCommand(new BotStatsCmd());
        registerCommand(new ProfileCmd());
        registerCommand(new RenameCmd());
        registerCommand(new SetFriendLink());
        registerCommand(new InviteCmd());
        registerCommand(new CarsLBCmd());
        registerCommands(HSTools.jda);
    }

    public void registerCommand(SlashCommand command) {
        commands.put(command.getName(), command);
    }

    public void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
                commands.values().stream()
                        .map(SlashCommand::getCommandData)
                        .toList()
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommand command = commands.get(event.getName());
        Member member = event.getMember();
        if (command != null) {
            if (!HSUser.isRegistered(member.getId())) {
                SQLUserManager.createUser(member.getId(), member.getEffectiveName());
                new HSUser(member.getId());

                //event.getChannel().sendMessage("<@" + member.getId() + "> Welcome! I've registered you as a Hot Slide player under the name `" + member.getEffectiveName() + "`\n\n Use `/help` to change your name, add your friend code, or set up your cars and parts for display").queue();
            }

            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

            String options = event.getOptions().isEmpty()
                    ? "No options"
                    : event.getOptions().stream()
                    .map(opt -> opt.getName() + "=" + opt.getAsString())
                    .collect(Collectors.joining(", "));

            System.out.println(String.format("[%s] %s used /%s | Options: %s",
                    time,
                    member.getUser().getAsTag(),
                    command.getName(),
                    options
            ));

            command.execute(event);
        }
    }
}