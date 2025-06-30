package com.kasp.hstools.utils;

import com.kasp.hstools.HSTools;
import com.kasp.hstools.database.LiveryDAO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Liveries {

    private static final LiveryDAO dao = LiveryDAO.getInstance();

    public static void readLiveries() {
        System.out.println("Reading all liveries");

        Guild guild = HSTools.jda.getGuildById("429201334739140608");

        int created = 0;
        int updated = 0;

        if (guild == null) return;

        ForumChannel channel = guild.getForumChannelById("1353852869148676221");

        if (channel == null) return;

        List<ThreadChannel> allThreads = new ArrayList<>();

        allThreads.addAll(channel.getThreadChannels());
        allThreads.addAll(channel.retrieveArchivedPublicThreadChannels().complete());

        for (ThreadChannel c : allThreads) {
            if (c.getId().equals("1358328485071818782")) continue;

            String fixedCName = c.getName().replaceAll("—", "-");

            String[] idiotFix = fixedCName.split(" ");
            idiotFix[0] = idiotFix[0].replaceAll("-", " -");
            fixedCName = "";
            for (String s : idiotFix) {
                fixedCName += s + " ";
            }

            String[] cNameData = fixedCName.split(" ");
            cNameData[0] = cNameData[0].replaceAll("-", " -");

            String id = c.getId();
            String poster_id = c.getOwnerId();
            String poster_name = "";
            String livery_name = "";
            String car = "";
            String livery_link = "";
            String image = "";

            int finishedInt = 0;

            for (int i = 0; i < cNameData.length; i++) {
                String s = cNameData[i];

                if (s.equals("-") || s.equals("by")) {
                    finishedInt = i;
                    break;
                }

                car += s + " ";
            }

            if (cNameData[finishedInt].equals("by")) {
                poster_name = cNameData[finishedInt+1];
            } else if (cNameData[finishedInt].equals("-")) {
                for (int i = finishedInt+1; i < cNameData.length; i++) {
                    if (cNameData[i].equals("by")) {
                        poster_name = cNameData[i+1].substring(1);
                        break;
                    }

                    livery_name += cNameData[i] + " ";
                }
            }

            if (livery_name.equals("")) {
                livery_name = "No Name Provided";
            }

            livery_name = livery_name.replaceAll("\"", "").replaceAll("“", "").replaceAll("”", "").trim();
            car = car.trim().split(" ")[0].toLowerCase();
            car = car.substring(0, 1).toUpperCase() + car.substring(1);
            if (car.equalsIgnoreCase("time")) car = "Time Machine";
            if (car.equalsIgnoreCase("kajiu")) car = "Kaiju";

            if (poster_name != null) {
                poster_name = poster_name.replaceAll(",", "");
            }
            if (poster_name == null || poster_name.equals("")) {
                if (guild.getMemberById(poster_id) != null) {
                    poster_name = guild.getMemberById(poster_id).getEffectiveName();
                }
                else {
                    poster_name = "Unknown";
                }
            }

            int upvotes = 1;
            List<Message> messages = c.getIterableHistory().reverse().limit(1).complete();
            Message firstMsg = messages.getFirst();
            if (firstMsg != null) {
                if (!firstMsg.getReactions().isEmpty())
                    upvotes = firstMsg.getReactions().getFirst().getCount();
                livery_link = extractLink(firstMsg.getContentRaw());
                image = firstMsg.getAttachments().getFirst().getUrl();
            }

            if (dao.liveryExists(id)) {
                dao.updateLivery(id, poster_name, livery_name, car, livery_link, image, upvotes);
                updated++;
            } else {
                dao.createLivery(id, poster_id, poster_name, livery_name, car, livery_link, image, upvotes);
                created++;
            }
        }

        System.out.println("Liveries read successfully. Created: " + created + " | Updated: " + updated);
    }

    private static String extractLink(String text) {
        String urlRegex = "(hotslide?://\\S+)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "-";
        }
    }
}
