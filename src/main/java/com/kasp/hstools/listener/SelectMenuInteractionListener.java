package com.kasp.hstools.listener;

import com.kasp.hstools.EmbedType;
import com.kasp.hstools.HSTools;
import com.kasp.hstools.command.SlashCommand;
import com.kasp.hstools.instance.Embed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectMenuInteractionListener extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("choose-help-topic")) {
            if (event.getSelectedOptions().getFirst().getValue().equals("all-cmds")) {
                displayAllCommands(event);
            }
            else if (event.getSelectedOptions().getFirst().getValue().equals("car-setup")) {
                displayCarSetupInfo(event);
            }
            else if (event.getSelectedOptions().getFirst().getValue().equals("profile-setup")) {
                displayProfileSetupInfo(event);
            }
        }
    }

    private void displayAllCommands(StringSelectInteractionEvent event) {
        Map<String, SlashCommand> commands = HSTools.commandManager.getAllCommands();
        List<SlashCommand> commandList = new ArrayList<>(commands.values()); // Convert to list

        InteractionHook hook = event.replyEmbeds(new EmbedBuilder().setTitle("loading...").build()).setEphemeral(true).complete();
        Message embedMsg = hook.retrieveOriginal().complete();

        int totalPages = (int) Math.ceil(commandList.size() / 10.0);

        for (int j = 0; j < commandList.size(); j += 10) {
            Embed reply = new Embed(EmbedType.DEFAULT, "All commands", "", totalPages);

            StringBuilder desc = new StringBuilder();
            for (int i = 0; i < 10 && i + j < commandList.size(); i++) {
                SlashCommand cmd = commandList.get(i + j);
                desc.append("- `/").append(cmd.getName()).append("` - ").append(cmd.getDescription()).append("\n");
            }
            reply.setDescription(desc.toString());

            if (j == 0) {
                embedMsg.editMessageEmbeds(reply.build())
                        .setActionRow(Embed.createButtons(reply.getCurrentPage()))
                        .queue();
            }

            Embed.addPage(embedMsg.getId(), reply);
        }
    }

    private void displayCarSetupInfo(StringSelectInteractionEvent event) {
        Embed embed = new Embed(EmbedType.DEFAULT, "Car Setup Guide", "Once you set up all your cars using an external website, you can view them anytime by doing `/cars`, theres also an option to view anyone else's cars, compare the garages (between up to 10 people)", 1);
        embed.addField("Setup Process", "Follow these steps to set up your garage:\n" +
                "1. Use `/setupcars`, go to the website linked\n" +
                "2. Follow the instructions on the website (use drop down menus to set up car ranks)\n" +
                "3. Copy the generated text at the bottom, switch back to discord\n" +
                "4. Use `/loadcardata` and paste the generated text to complete the setup\n" +
                "5. You can now view your garage by using `/cars`"
                , false);
        embed.addField("Useful info / commands",
                "- If you upgrade a car in-game you do not have to set everything up from nothing again, `/setupcars` detects if you have completed the setup at least once and gives you another link to modify your existing garage\n" +
                "- You can view anyone else's car garage by using `/cars` and specifying the user (discord mention)\n" +
                "- You can compare up to 10 people's garages by using `/sharedcars` and specifying them (you can also specify yourself as one of them)"
                ,false);
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void displayProfileSetupInfo(StringSelectInteractionEvent event) {
        Embed embed = new Embed(EmbedType.DEFAULT, "Profile Setup Guide", "`/profile` currently displays user's owned car count, friend link", 1);
        embed.addField("Setup Process", "Follow these steps to set up your profile:\n" +
                        "1. Complete car garage setup process (`/setupcars`)\n" +
                        "2. Change your name to your in-game name using `/rename` *this won't affect your discord name or nickname, it's only used when bot needs to display user related information in its own commands*\n" +
                        "3. Set your friend link using `/setfriendlink` for it to show up in `/profile`. You can also disable showing it by setting it to `-`\n" +
                        "4. Done! You can view your own profile by using `/profile` or someone else's using `/profile <@mention>`\n"
                , false);
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
