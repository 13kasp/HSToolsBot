package com.kasp.hstools;

import com.kasp.hstools.command.CommandManager;
import com.kasp.hstools.config.Config;
import com.kasp.hstools.database.SQLTableManager;
import com.kasp.hstools.database.SQLite;
import com.kasp.hstools.instance.Car;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.listener.CmdAutoCompleteListener;
import com.kasp.hstools.listener.PagesEventsListener;
import com.kasp.hstools.utils.Emojis;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.JDALogger;

import javax.security.auth.login.LoginException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HSTools {

    public static JDA jda;
    public static Guild guild;
    public static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        Config.loadConfig();
        Emojis.setupEmojis();

        if (Config.getValue("token") == null) {
            System.out.println("[!] Please set your token in config.yml");
            return;
        }

        try {
            buildJDA();

            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n[!] Finishing up (loading commands, connecting to the database)...\n");

        //
        // CONNECTING TO DB
        //

        SQLite.connect();
        SQLTableManager.createUsersTable();

        //
        // LOADING ALL USERS
        //

        List<String> users = new ArrayList<>();

        try {
            ResultSet rs = SQLite.queryData("SELECT * FROM users");
            while (rs.next()) {
                users.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[!] There was a problem loading all data");
        }

        for (String s : users) {
            try {
                new HSUser(s);
            } catch (Exception e) {
                System.out.println("[!] a player could not be loaded! Please make a bug report on support discord asap");
            }
        }

        //
        // LOADING ALL COMMANDS
        //

        CommandManager commandManager = new CommandManager();
        jda.addEventListener(commandManager);

        initAllCars();

        System.out.println("\n[!] Bot has successfully started\n");
    }

    private static void buildJDA() throws LoginException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(Config.getValue("token"));
        jdaBuilder.setStatus(OnlineStatus.valueOf(Config.getValue("status").toUpperCase()));
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        jdaBuilder.addEventListeners(new PagesEventsListener(), new CmdAutoCompleteListener());

        JDALogger.setFallbackLoggerEnabled(false);
        jda = jdaBuilder.build();
    }

    private static void initAllCars() {
        new Car("kaiju", CarType.STREET, 130, 151, 120, 124, 15, 10, 10, 10, 10,  Map.of("midacc", 6));
        new Car("millennium", CarType.STREET, 122, 127, 130, 145, 10, 15, 10, 10, 10,  Map.of("handling", 6));
    }
}