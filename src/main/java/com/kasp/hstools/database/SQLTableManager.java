package com.kasp.hstools.database;

public class SQLTableManager {

    public static void createUsersTable() {
        SQLite.updateData("CREATE TABLE IF NOT EXISTS users(" +
                "discordID TEXT," +
                "ign TEXT," +
                "cars TEXT," +
                "parts TEXT);");
    }
}
