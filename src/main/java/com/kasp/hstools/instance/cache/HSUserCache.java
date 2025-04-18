package com.kasp.hstools.instance.cache;

import com.kasp.hstools.instance.HSUser;

import java.util.HashMap;
import java.util.Map;

public class HSUserCache {

    private static HashMap<String, HSUser> users = new HashMap<>();

    public static HSUser getUser(String ID) {
        return users.get(ID);
    }

    public static void addUser(HSUser user) {
        users.put(user.getID(), user);

        System.out.println("User " + user.getIgn() + " (" + user.getID() + ")" + " has been loaded into memory");
    }

    public static void removeUser(HSUser user) {
        users.remove(user.getID());
    }

    public static boolean containsUser(HSUser user) {
        return users.containsValue(user);
    }

    public static void initializeUser(HSUser user) {
        if (!containsUser(user))
            addUser(user);
    }

    public static Map<String, HSUser> getUsers() {
        return users;
    }
}
