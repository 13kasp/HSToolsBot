package com.kasp.hstools.database;

import com.kasp.hstools.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        String host = Config.getValue("db_host");
        String port = Config.getValue("db_port");
        URL = "jdbc:mysql://" + host + ":" + port + "/s170786_liveries";
        USER = Config.getValue("db_user");
        PASSWORD = Config.getValue("db_pass");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
