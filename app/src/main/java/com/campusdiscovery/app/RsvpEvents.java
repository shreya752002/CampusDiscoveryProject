package com.campusdiscovery.app;
;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class RsvpEvents extends AppCompatActivity {
    ListView rsvpList;
    ArrayAdapter<String> adapter;
    Button backButton;
    static Event selectedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp_events);
        rsvpList = findViewById(R.id.rsvpScroll);
        ArrayList<String> eventsNames = new ArrayList<>();

        for (String eventName: MainActivity.currentUser.rsvpStatus.keySet()) {
            if (MainActivity.currentUser.rsvpStatus.get(eventName).compareTo(RsvpType.WILL_ATTEND) == 0) {
                eventsNames.add(eventName);
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsNames);
        rsvpList.setAdapter(adapter);
        rsvpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedEventName = (String) rsvpList.getItemAtPosition(i);
                showEvent(selectedEventName);
            }
        });
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToEvents();
            }
        });
    }

    private void backToEvents() {
        Intent intent = new Intent(this, EventScreen.class);
        startActivity(intent);
    }

    public void showEvent(String eventName) {
        ExpandedEvent.flag = true;
        Intent intent = new Intent(this, ExpandedEvent.class);
        selectedEvent = MainActivity.events.get(eventName);
        startActivity(intent);
    }
}