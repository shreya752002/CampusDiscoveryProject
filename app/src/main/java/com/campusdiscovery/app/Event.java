package com.campusdiscovery.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class Event implements Serializable {
    String eventName;
    Location location;
    Date date;
    String eventId;
    int hour;
    int min;
    String description;
    User creator;
    Boolean inviteOnly;
    int totalCapacity;
    int currentNumAttendees;
    List<String> willAttend;
    List<String> willNotAttend;
    List<String> maybeAttend;
    List<String> nemesisUsers;
    HashSet<String> inviteeList;

    public Event (String name, Location location, String id, String desc, int capacity,
                  Boolean inviteOnly, int hour, int min) {
        this.eventName = name;
        this.location = location;
        this.eventId = id;
        this.description = desc;
        this.totalCapacity = capacity;
        this.inviteOnly = inviteOnly;
        this.hour = hour;
        this.min = min;
        willNotAttend = new ArrayList<>();
        willAttend = new ArrayList<>();
        maybeAttend = new ArrayList<>();
        nemesisUsers = new ArrayList<>();
    }

}
