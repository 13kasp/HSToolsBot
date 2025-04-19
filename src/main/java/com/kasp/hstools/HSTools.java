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
        new Car("tofu", CarType.STREET, 104, 113, 90, 106, Map.of("midacc", 6));
        new Car("the_krystal_ship", CarType.MUSCLE, 85, 106, 121, 100, Map.of("topspeed", 6));
        new Car("tow_truck", CarType.MUSCLE, 84, 107, 120, 102, Map.of("topspeed", 6));
        new Car("taxi", CarType.MUSCLE, 83, 108, 117, 104, Map.of("topspeed", 6));
        new Car("neon", CarType.STREET, 102, 115, 92, 104, Map.of("midacc", 6));
        new Car("timber", CarType.SUV, 121, 115, 82, 95, Map.of("earlyacc", 6));
        new Car("polluter", CarType.STREET, 102, 113, 93, 104, Map.of("midacc", 6));
        new Car("safari", CarType.SUV, 118, 120, 85, 90, Map.of("earlyacc", 6));
        new Car("minni", CarType.STREET, 103, 114, 91, 104, Map.of("midacc", 6));
        new Car("agent", CarType.SUV, 121, 115, 82, 95, Map.of("earlyacc", 6));
        new Car("atomic", CarType.MUSCLE, 82, 107, 119, 104, Map.of("topspeed", 6));
        new Car("roamer", CarType.SUV, 115, 119, 82, 96, Map.of("earlyacc", 6));
        new Car("fortress", CarType.SUV, 133, 127, 88, 102, Map.of("earlyacc", 6));
        new Car("flower", CarType.MUSCLE, 95, 116, 131, 107, Map.of("topspeed", 6));
        new Car("leap", CarType.SUPER, 105, 111, 113, 120, Map.of("handling", 6));
        new Car("satin", CarType.SUPER, 110, 106, 111, 123, Map.of("handling", 6));
        new Car("koi", CarType.STREET, 110, 127, 101, 113, Map.of("midacc", 6));
        new Car("wingman", CarType.STREET, 114, 124, 98, 114, Map.of("midacc", 6));
        new Car("nightown", CarType.MUSCLE, 92, 118, 132, 108, Map.of("topspeed", 6));
        new Car("hopper", CarType.SUV, 130, 127, 91, 101, Map.of("earlyacc", 6));
        new Car("horizon", CarType.STREET, 112, 126, 104, 108, Map.of("midacc", 6));
        new Car("chinese_van", CarType.STREET, 109, 123, 104, 114, Map.of("midacc", 6));
        new Car("firearrow", CarType.MUSCLE, 93, 113, 133, 111, Map.of("topspeed", 6));
        new Car("electron", CarType.SUPER, 110, 110, 110, 121, Map.of("handling", 6));
        new Car("wild_west", CarType.MUSCLE, 93, 119, 130, 108, Map.of("topspeed", 6));
        new Car("coupe", CarType.SUPER, 106, 110, 113, 121, Map.of("handling", 6));
        new Car("monster_car", CarType.SUV, 132, 129, 89, 100, Map.of("earlyacc", 6));
        new Car("hammer", CarType.SUV, 131, 127, 92, 100, Map.of("earlyacc", 6));
        new Car("cyber", CarType.SUV, 147, 133, 96, 111, Map.of("earlyacc", 6));
        new Car("mamba", CarType.STREET, 120, 141, 106, 120, Map.of("midacc", 6));
        new Car("ronin", CarType.STREET, 120, 136, 112, 119, Map.of("midacc", 6));
        new Car("time_machine", CarType.SUPER, 116, 116, 122, 132, Map.of("handling", 6));
        new Car("bandit", CarType.MUSCLE, 103, 122, 144, 115, Map.of("topspeed", 6));
        new Car("predator", CarType.SUV, 143, 134, 101, 110, Map.of("earlyacc", 6));
        new Car("maverick", CarType.MUSCLE, 101, 123, 146, 117, Map.of("topspeed", 6));
        new Car("comet", CarType.STREET, 120, 139, 109, 119, Map.of("midacc", 6));
        new Car("fury", CarType.SUPER, 117, 116, 123, 131, Map.of("handling", 6));
        new Car("hornet", CarType.MUSCLE, 100, 122, 147, 118, Map.of("topspeed", 6));
        new Car("ghost", CarType.SUPER, 115, 119, 119, 134, Map.of("handling", 6));
        new Car("bison", CarType.SUV, 144, 135, 99, 109, Map.of("earlyacc", 6));
        new Car("protos", CarType.SUV, 84, 142, 154, 144, Map.of("earlyacc", 6));
        new Car("millennium", CarType.SUPER, 122, 127, 130, 145, Map.of("handling", 6));
        new Car("mantis", CarType.STREET, 126, 154, 118, 126, Map.of("midacc", 6));
        new Car("startup", CarType.SUPER, 120, 124, 132, 149, Map.of("handling", 6));
        new Car("jackpot", CarType.MUSCLE, 106, 133, 158, 124, Map.of("topspeed", 6));
        new Car("expedition", CarType.SUV, 157, 145, 106, 116, Map.of("earlyacc", 6));
        new Car("glamour", CarType.STREET, 130, 151, 115, 128, Map.of("midacc", 6));
        new Car("ace", CarType.SUPER, 121, 126, 129, 148, Map.of("handling", 6));
        new Car("memphis", CarType.MUSCLE, 112, 129, 155, 128, Map.of("topspeed", 6));
        new Car("project", CarType.SUV, 154, 142, 110, 119, Map.of("earlyacc", 6));
        new Car("apex", CarType.SUV, 160, 142, 104, 118, Map.of("earlyacc", 6));
        new Car("kobra", CarType.STREET, 128, 154, 114, 128, Map.of("midacc", 6));
        new Car("rodeo", CarType.MUSCLE, 106, 131, 159, 128, Map.of("topspeed", 6));
        new Car("centaur", CarType.SUPER, 126, 123, 131, 144, Map.of("handling", 6));
        new Car("thor", CarType.SUPER, 125, 126, 128, 145, Map.of("handling", 6));
        new Car("merlin", CarType.SUPER, 124, 124, 127, 149, Map.of("topspeed", 6));
        new Car("expedition", CarType.SUV, 158, 141, 109, 116, Map.of("topspeed", 6));
        new Car("duster", CarType.STREET, 127, 152, 116, 130, Map.of("midacc", 6));
        new Car("kaiju", CarType.STREET, 130, 151, 120, 124, Map.of("midacc", 6));
        new Car("fang", CarType.MUSCLE, 111, 132, 156, 125, Map.of("topspeed", 6));
    }
}