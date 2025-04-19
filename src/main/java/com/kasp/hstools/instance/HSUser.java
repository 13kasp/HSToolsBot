package com.kasp.hstools.instance;

import com.kasp.hstools.database.SQLUserManager;
import com.kasp.hstools.database.SQLite;
import com.kasp.hstools.instance.cache.HSUserCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HSUser {

    private String ID;
    private String ign;
    private Map<String, String> cars = new HashMap<>();
    private Map<String, String> parts = new HashMap<>();
    private String friendLink;

    public HSUser(String ID) {
        this.ID = ID;

        ResultSet resultSet = SQLite.queryData("SELECT * FROM users WHERE discordID='" + ID + "';");

        try {
            this.ign = resultSet.getString(2);

            String[] db_cars = resultSet.getString(3).split(",");
            String[] db_parts = resultSet.getString(4).split(",");

            for (String s : db_cars) {
                if (s.contains("#")) {
                    cars.put(s.split("#")[0], s.split("#")[1]);
                }
            }

            for (String s : db_parts) {
                if (s.contains("#")) {
                    parts.put(s.split("#")[0], s.split("#")[1]);
                }
            }

            this.friendLink = resultSet.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        HSUserCache.initializeUser(this);
    }

    public static Map<String, String> findSharedCars(List<HSUser> users) {
        if (users.isEmpty()) return Collections.emptyMap();

        Map<String, String> sharedCars = users.get(0).getCars().entrySet().stream()
                .filter(e -> !"X".equalsIgnoreCase(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (int i = 1; i < users.size() && !sharedCars.isEmpty(); i++) {
            Map<String, String> currentCars = users.get(i).getCars();

            sharedCars.entrySet().removeIf(entry -> {
                String carName = entry.getKey();
                String requiredRank = entry.getValue();
                String actualRank = currentCars.get(carName);
                return actualRank == null ||
                        !actualRank.equals(requiredRank) ||
                        "X".equalsIgnoreCase(actualRank);
            });
        }

        return sharedCars;
    }

    public static boolean isRegistered(String ID) {
        return SQLUserManager.isRegistered(ID);
    }

    public String getID() {
        return ID;
    }

    public String getIgn() {
        return ign;
    }

    public Map<String, String> getCars() {
        return cars;
    }

    public Map<String, String> getParts() {
        return parts;
    }

    public String getFriendLink() {
        return friendLink;
    }

    public void setIgn(String ign) {
        this.ign = ign;
        SQLUserManager.updateIgn(ID);
    }

    public void setCars(Map<String, String> cars) {
        this.cars = cars;
        SQLUserManager.updateCars(ID);
    }

    public void setParts(Map<String, String> parts) {
        this.parts = parts;
        SQLUserManager.updateParts(ID);
    }

    public void setFriendLink(String friendLink) {
        this.friendLink = friendLink;
        SQLUserManager.updateFriendLink(ID);
    }
}
