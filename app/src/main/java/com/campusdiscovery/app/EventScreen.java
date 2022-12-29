package com.campusdiscovery.app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.campusdiscovery.app.UserType.INDEPENDENT_USER;

public class EventScreen extends AppCompatActivity {
    FloatingActionButton newEventButton;
    ListView listEvents;
    ArrayAdapter<String> adapter;
    static Event selectedEvent;
    FloatingActionButton logOut;
    FloatingActionButton rsvpEvents;
    Button mapView;
    Spinner filters;
    ArrayAdapter<String> filterAdapter;
    Button filterButton;
    Button clearButton;
    String filterText;
    ArrayList<String> eventsNames = new ArrayList<>();
    DatePickerDialog.OnDateSetListener eventDateSetListener;
    Date chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);
        filters = findViewById(R.id.filterEvents);
        String[] items;
        items = new String[]{"Host ID", "Event Name", "Location"};
        filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        filters.setAdapter(filterAdapter);

        filterButton = (Button) findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter();
            }
        });

        eventDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                chosenDate = new Date(year, month, day);
//                    String date = month + "/" + day + "/" + year;
            }
        };

        listEvents = findViewById(R.id.scrollEevents);
        if (!MainActivity.check) {
            System.out.println("In loop");
            for (int i = 0; i < MainActivity.events.size(); i++) {
                eventsNames.add(MainActivity.eventsList.get(i).eventName);
            }
        } else {
            System.out.println(MainActivity.filteredEvents.size());
            for (int i = 0; i < MainActivity.filteredEvents.size(); i++) {
                eventsNames.add(MainActivity.filteredEvents.get(i).eventName);
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsNames);

        listEvents.setAdapter(adapter);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedEventName = (String) listEvents.getItemAtPosition(i);
                showEvent(selectedEventName);
            }
        });
        newEventButton = findViewById(R.id.createEventButton);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
        logOut = (FloatingActionButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        rsvpEvents = (FloatingActionButton) findViewById(R.id.rsvpEvents);
        rsvpEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsvpList();
            }
        });
        mapView = (Button) findViewById(R.id.mapView);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMap();
            }
        });

        clearButton = (Button) findViewById(R.id.clearFilter);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
            }
        });
    }

    private void clearFilter() {
        MainActivity.check = false;
        Intent intent = new Intent(this, EventScreen.class);
        startActivity(intent);
    }

    public void showEvent(String eventName) {
        Intent intent = new Intent(this, ExpandedEvent.class);
        selectedEvent = MainActivity.events.get(eventName);
        startActivity(intent);
    }

    public void createEvent() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void logOut() {
        Intent intent = new Intent(this, UserSelectScreen.class);
        startActivity(intent);
    }

    public void rsvpList() {
        Intent intent = new Intent(this, RsvpEvents.class);
        startActivity(intent);
    }

    public void switchToMap() {
        Intent intent = new Intent(this, EventMap.class);
        startActivity(intent);
    }

    public void filter() {
        MainActivity.check = true;
        MainActivity.filteredEvents.clear();
        Intent intent = new Intent(this, EventScreen.class);
        String chosenFilter = filters.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by " + chosenFilter);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        if (chosenFilter.equals("Location")) {
            builder.setMessage("Locations: Coc, Scheller, Tech green, Culc, Exhibition Hall");
        }
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterText = input.getText().toString();
                for (int i = 0; i < MainActivity.events.size(); i++) {
                    Event event = MainActivity.eventsList.get(i);
                    if (chosenFilter.compareTo("Host ID") == 0 &&
                            event.creator.uniqueId.toLowerCase()
                                    .compareTo(filterText.toLowerCase()) == 0) {
                        MainActivity.filteredEvents.add(event);
                    } else if (chosenFilter.compareTo("Event Name") == 0
                            && event.eventName.toLowerCase()
                            .compareTo(filterText.toLowerCase()) == 0) {
                        MainActivity.filteredEvents.add(event);
                    } else if (event.location.location.equalsIgnoreCase(filterText)) {
                        MainActivity.filteredEvents.add(event);
                    }
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}