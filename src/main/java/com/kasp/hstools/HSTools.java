package com.kasp.hstools;

import com.kasp.hstools.command.CommandManager;
import com.kasp.hstools.config.Config;
import com.kasp.hstools.database.SQLTableManager;
import com.kasp.hstools.database.SQLite;
import com.kasp.hstools.database.ExternalDB;
import com.kasp.hstools.instance.Car;
import com.kasp.hstools.instance.HSUser;
import com.kasp.hstools.listener.CmdAutoCompleteListener;
import com.kasp.hstools.listener.PagesEventsListener;
import com.kasp.hstools.listener.SelectMenuInteractionListener;
import com.kasp.hstools.utils.Emojis;
import com.kasp.hstools.utils.Liveries;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HSTools {

    public static JDA jda;
    public static Guild guild;
    public static final String VERSION = "1.1.0";
    public static CommandManager commandManager;

    public static void main(String[] args) throws ClassNotFoundException {
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

        Class.forName("org.sqlite.JDBC");
        Class.forName("com.mysql.cj.jdbc.Driver");

        SQLite.connect();
        SQLTableManager.createUsersTable();
        ExternalDB.connect();

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

        commandManager = new CommandManager();
        jda.addEventListener(commandManager);

        initAllCars();

        System.out.println("\n[!] Bot has successfully started\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Liveries.readLiveries();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static void buildJDA() throws LoginException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(Config.getValue("token"));
        jdaBuilder.setStatus(OnlineStatus.valueOf(Config.getValue("status").toUpperCase()));
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        jdaBuilder.addEventListeners(new PagesEventsListener(), new CmdAutoCompleteListener(), new SelectMenuInteractionListener());

        JDALogger.setFallbackLoggerEnabled(false);
        jda = jdaBuilder.build();
    }

    private static void initAllCars() {
        new Car("tofu", CarType.STREET, 104, 113, 90, 106);
        new Car("the_krystal_ship", CarType.MUSCLE, 85, 106, 121, 100);
        new Car("tow_truck", CarType.MUSCLE, 84, 107, 120, 102);
        new Car("taxi", CarType.MUSCLE, 83, 108, 117, 104);
        new Car("neon", CarType.STREET, 102, 115, 92, 104);
        new Car("timber", CarType.SUV, 121, 115, 82, 95);
        new Car("polluter", CarType.STREET, 102, 113, 93, 104);
        new Car("safari", CarType.SUV, 118, 120, 85, 90);
        new Car("minni", CarType.STREET, 103, 114, 91, 104);
        new Car("agent", CarType.SUV, 121, 115, 82, 95);
        new Car("atomic", CarType.MUSCLE, 82, 107, 119, 104);
        new Car("roamer", CarType.SUV, 115, 119, 82, 96);
        new Car("fortress", CarType.SUV, 133, 127, 88, 102);
        new Car("flower", CarType.MUSCLE, 95, 116, 131, 107);
        new Car("leap", CarType.SUPER, 105, 111, 113, 120);
        new Car("satin", CarType.SUPER, 110, 106, 111, 123);
        new Car("koi", CarType.STREET, 110, 127, 101, 113);
        new Car("wingman", CarType.STREET, 114, 124, 98, 114);
        new Car("nightown", CarType.MUSCLE, 92, 118, 132, 108);
        new Car("hopper", CarType.SUV, 130, 127, 91, 101);
        new Car("horizon", CarType.STREET, 112, 126, 104, 108);
        new Car("chinese_van", CarType.STREET, 109, 123, 104, 114);
        new Car("firearrow", CarType.MUSCLE, 93, 113, 133, 111);
        new Car("electron", CarType.SUPER, 110, 110, 110, 121);
        new Car("wild_west", CarType.MUSCLE, 93, 119, 130, 108);
        new Car("coupe", CarType.SUPER, 106, 110, 113, 121);
        new Car("monster_car", CarType.SUV, 132, 129, 89, 100);
        new Car("hammer", CarType.SUV, 131, 127, 92, 100);
        new Car("cyber", CarType.SUV, 147, 133, 96, 111);
        new Car("mamba", CarType.STREET, 120, 141, 106, 120);
        new Car("ronin", CarType.STREET, 120, 136, 112, 119);
        new Car("time_machine", CarType.SUPER, 116, 116, 122, 132);
        new Car("bandit", CarType.MUSCLE, 103, 122, 144, 115);
        new Car("predator", CarType.SUV, 143, 134, 101, 110);
        new Car("maverick", CarType.MUSCLE, 101, 123, 146, 117);
        new Car("comet", CarType.STREET, 120, 139, 109, 119);
        new Car("fury", CarType.SUPER, 117, 116, 123, 131);
        new Car("hornet", CarType.MUSCLE, 100, 122, 147, 118);
        new Car("ghost", CarType.SUPER, 115, 119, 119, 134);
        new Car("bison", CarType.SUV, 144, 135, 99, 109);
        new Car("protos", CarType.SUV, 84, 142, 154, 144);
        new Car("millennium", CarType.SUPER, 122, 127, 130, 145);
        new Car("mantis", CarType.STREET, 126, 154, 118, 126);
        new Car("startup", CarType.SUPER, 120, 124, 132, 149);
        new Car("jackpot", CarType.MUSCLE, 106, 133, 158, 124);
        new Car("expedition", CarType.SUV, 157, 145, 106, 116);
        new Car("glamour", CarType.STREET, 130, 151, 115, 128);
        new Car("ace", CarType.SUPER, 121, 126, 129, 148);
        new Car("memphis", CarType.MUSCLE, 112, 129, 155, 128);
        new Car("project", CarType.SUV, 154, 142, 110, 119);
        new Car("apex", CarType.SUV, 160, 142, 104, 118);
        new Car("kobra", CarType.STREET, 128, 154, 114, 128);
        new Car("rodeo", CarType.MUSCLE, 106, 131, 159, 128);
        new Car("centaur", CarType.SUPER, 126, 123, 131, 144);
        new Car("thor", CarType.SUPER, 125, 126, 128, 145);
        new Car("merlin", CarType.SUPER, 124, 124, 127, 149);
        new Car("expedition", CarType.SUV, 158, 141, 109, 116);
        new Car("duster", CarType.STREET, 127, 152, 116, 130);
        new Car("kaiju", CarType.STREET, 130, 151, 120, 124);
        new Car("fang", CarType.MUSCLE, 111, 132, 156, 125);
        new Car("spectre", CarType.MUSCLE, 109, 129, 158, 128);
        new Car("conqueror", CarType.SUV, 158, 141, 109, 116);
        new Car("quantum", CarType.SUPER, 146, 118, 114, 147);
        new Car("samurai", CarType.STREET, 136, 152, 108, 130);
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}