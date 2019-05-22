package net;

import java.util.HashMap;

public class ListUsers {
    private HashMap<String, Integer> users;

    public ListUsers() {
        this.users = new HashMap<>();
    }


    public HashMap<String, Integer> getUsers() {
        return users;
    }
}
