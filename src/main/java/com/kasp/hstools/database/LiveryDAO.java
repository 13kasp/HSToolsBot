package com.kasp.hstools.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LiveryDAO {
    private static final LiveryDAO instance = new LiveryDAO();

    public static LiveryDAO getInstance() {
        return instance;
    }

    public boolean liveryExists(String id) {
        String sql = "SELECT 1 FROM liveries WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createLivery(String id, String posterId, String posterName, String liveryName, String car, String liveryLink, String image, int upvotes) {
        String sql = "INSERT INTO liveries (id, poster_id, poster_name, livery_name, car, livery_link, image, upvotes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, posterId);
            stmt.setString(3, posterName);
            stmt.setString(4, liveryName);
            stmt.setString(5, car);
            stmt.setString(6, liveryLink);
            stmt.setString(7, image);
            stmt.setInt(8, upvotes);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLivery(String id, String posterName, String liveryName, String car, String liveryLink, String image, int upvotes) {
        String sql = "UPDATE liveries SET poster_name = ?, livery_name = ?, car = ?, livery_link = ?, image = ?, upvotes = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, posterName);
            stmt.setString(2, liveryName);
            stmt.setString(3, car);
            stmt.setString(4, liveryLink);
            stmt.setString(5, image);
            stmt.setInt(6, upvotes);
            stmt.setString(7, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
