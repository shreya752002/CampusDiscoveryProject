package com.campusdiscovery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    static HashMap<User, Event[]> usersAndEvents;
    static HashMap<String, Event> events;
    static HashMap<String, User> users;
    static User currentUser;
    static ArrayList<Event> eventsList;
    static boolean check = false;
    static ArrayList<Event> filteredEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users = new HashMap<>();
        usersAndEvents = new HashMap<>();
        events = new HashMap<>();
        eventsList = new ArrayList<>();
        User u1 = new User("Sara", "abcd", UserType.INDEPENDENT_USER, "sara1");
        users.put(u1.uniqueId, u1);
        Location location1 = CreateEvent.getLocation("CoC");
        Location location2 = CreateEvent.getLocation("Scheller");
        Location location3 = CreateEvent.getLocation("Culc");
        Event newEvent = new Event("Networking", location1, "netw1",
                "Meet and greet", 50, false, 12, 30);
        newEvent.date = new Date(2023, 02, 21);
        newEvent.creator = users.get("sara1");
        events.put(newEvent.eventName, newEvent);
        eventsList.add(newEvent);

        Event event1 = new Event("Meet and Greet", location2, "mg1",
                "Meet new people", 100, false, 12, 30);
        event1.date = new Date(2023, 02, 21);
        event1.creator = users.get("sara1");
        events.put(event1.eventName, event1);
        eventsList.add(event1);

        Event event2 = new Event("Workshop", location3, "wk1",
                "Learn new things", 200, false, 8, 00);
        event2.date = new Date(2022, 11, 25);
        event2.creator = users.get("sara1");
        events.put(event2.eventName, event2);
        eventsList.add(event2);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserSelect();
            }
        });
        filteredEvents = new ArrayList<>();
    }
    public void openUserSelect() {
        Intent intent = new Intent(this, UserSelectScreen.class);
        startActivity(intent);
    }
}