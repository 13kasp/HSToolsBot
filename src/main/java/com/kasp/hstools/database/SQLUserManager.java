package com.kasp.hstools.database;

import com.kasp.hstools.instance.cache.HSUserCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringJoiner;

public class SQLUserManager {

    public static void createUser(String ID, String ign) {
        SQLite.updateData("INSERT INTO users(discordID, ign, cars, parts)" +
                " VALUES('" + ID + "'," +
                "'" + ign + "'," +
                "''," + // cars
                "'');"); // parts
    }

    public static int getUsersSize() {
        ResultSet resultSet = SQLite.queryData("SELECT COUNT(discordID) FROM users");
        try {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean isRegistered(String ID) {
        ResultSet resultSet = SQLite.queryData("SELECT EXISTS(SELECT 1 FROM users WHERE discordID='" + ID + "');");

        try {
            if (resultSet.getInt(1) == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void updateIgn(String ID) {
        SQLite.updateData("UPDATE users SET ign = '" + HSUserCache.getUser(ID).getIgn() + "' WHERE discordID='" + ID + "';");
    }

    public static void updateCars(String ID) {
        StringJoiner joiner = new StringJoiner(",");
        Map<String, String> cars = HSUserCache.getUser(ID).getCars();

        for (Map.Entry<String, String> entry : cars.entrySet()) {
            joiner.add(entry.getKey() + "#" + entry.getValue());
        }

        SQLite.updateData("UPDATE users SET cars = '" + joiner + "' WHERE discordID='" + ID + "';");
    }

    public static void updateParts(String ID) {
        StringJoiner joiner = new StringJoiner(",");
        Map<String, String> parts = HSUserCache.getUser(ID).getParts();

        for (Map.Entry<String, String> entry : parts.entrySet()) {
            joiner.add(entry.getKey() + "#" + entry.getValue());
        }

        SQLite.updateData("UPDATE users SET parts = '" + joiner + "' WHERE discordID='" + ID + "';");
    }
}
