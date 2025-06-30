package com.kasp.hstools.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EDBLiveriesManager {

    public static void createLivery(String ID, String poster_id, String poster_name, String livery_name, String car, String livery_link, String image, int upvotes) {
        poster_name = escape(poster_name);
        livery_name = escape(livery_name);
        car = escape(car);
        livery_link = escape(livery_link);
        image = escape(image);


        ExternalDB.updateData("INSERT INTO liveries(id, poster_id, poster_name, livery_name, car, livery_link, image, upvotes)" +
                " VALUES('" + ID + "'," +
                "'" + poster_id + "'," +
                "'" + poster_name + "'," +
                "'" + livery_name + "'," +
                "'" + car + "'," +
                "'" + livery_link + "'," +
                "'" + image + "'," +
                upvotes + ");");
    }

    public static void updateLivery(String ID, String poster_name, String livery_name, String car, String livery_link, String image, int upvotes) {
        poster_name = escape(poster_name);
        livery_name = escape(livery_name);
        car = escape(car);
        livery_link = escape(livery_link);
        image = escape(image);

        ExternalDB.updateData("UPDATE liveries SET poster_name = '" + poster_name + "', livery_name = '" + livery_name + "', car = '" + car + "', livery_link = '" + livery_link + "', image = '" + image + "', upvotes = " + upvotes +
                        " WHERE id = '" + ID + "';");
    }

    public static boolean liveryExists(String ID) {
        ResultSet resultSet = ExternalDB.queryData("SELECT EXISTS(SELECT 1 FROM liveries WHERE id='" + ID + "');");

        try {
            if (resultSet.next() && resultSet.getInt(1) == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String escape(String input) {
        if (input == null) return "";
        return input.replaceAll("'", "''");
    }
}
