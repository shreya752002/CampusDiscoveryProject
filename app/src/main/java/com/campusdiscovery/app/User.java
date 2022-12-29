package com.campusdiscovery.app;

import java.util.HashMap;

public class User {
    String name;
    String password;
    UserType type;
    String uniqueId;
    HashMap<String, RsvpType> rsvpStatus;
    public User (String name, String password, UserType type, String uniqueId) {
        this.name = name;
        this.password = password;
        this.type = type;
        this.uniqueId = uniqueId;
        rsvpStatus = new HashMap<>();
    }
}
