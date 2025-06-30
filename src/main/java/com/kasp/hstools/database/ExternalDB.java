package com.kasp.hstools.database;

import com.kasp.hstools.config.Config;

import java.sql.*;

public class ExternalDB {

    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        connection = null;
        try {
            String host = Config.getValue("db_host");
            String port = Config.getValue("db_port");
            String user = Config.getValue("db_user");
            String pass = Config.getValue("db_pass");
            String url = "jdbc:mysql://" + host + ":" + port + "/s170786_liveries";

            connection = DriverManager.getConnection(url, user, pass);
            statement = connection.createStatement();
            System.out.println("[EDB] Successfully connected to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateData(String sql) {
        if (statement != null) {
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet queryData(String sql) {
        if (statement != null) {
            try {
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
