package com.kasp.hstools.listener;

import com.kasp.hstools.instance.Embed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PagesEventsListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getButton().getId().startsWith("hstools-page-")) {
            Message msg = event.getMessage();

            int number = Integer.parseInt(event.getButton().getId().replace("hstools-page-", ""));

            if (number <= -1) {
                event.reply("you're already on the first page").setEphemeral(true).queue();

                return;
            }

            if (Embed.embedPages.get(msg.getId()) == null) {
                event.reply("this embed does not have pages anymore. please generate a new one").setEphemeral(true).queue();
                return;
            }

            if (Embed.embedPages.get(msg.getId()).size() <= number) {
                event.reply("you're already on the last page").setEphemeral(true).queue();

                return;
            }

            event.deferEdit().queue();

            updatePage(msg, number);
        }
    }

    private void updatePage(Message msg, int number) {
        Embed embed = Embed.embedPages.get(msg.getId()).get(number);

        embed.setCurrentPage(number);
        msg.editMessageEmbeds(embed.build()).setActionRow(Embed.createButtons(embed.getCurrentPage())).queue();
    }
}
