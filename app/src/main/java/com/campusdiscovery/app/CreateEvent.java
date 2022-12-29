package com.campusdiscovery.app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import static com.campusdiscovery.app.UserType.INDEPENDENT_USER;

import com.google.android.gms.maps.model.LatLng;

public class CreateEvent extends AppCompatActivity {
    EditText eventNameInput;
    EditText eventIDInput;
    EditText eventDescInput;
    EditText eventTimehour;
    EditText eventTimemin;
    EditText eventCapacity;
    Switch inviteOnly;
    Button createEvent;
    Spinner dropdown;
    static String eventName;
    static String eventID;
    static String eventDesc;
    static int eventMin;
    static int eventHour;
    static Location location;
    static Boolean inviteOnlyValue;
    static int capacity;
    ArrayAdapter<String> adapter;
    AlertDialog.Builder builder;
    TextView eventDate;
    DatePickerDialog.OnDateSetListener eventDateSetListener;
    Date chosenDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User currentUSer = MainActivity.currentUser;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        dropdown = findViewById(R.id.locationEdit);
        String[] items;
        //sets dropdown based on logged in user type
        if (currentUSer.type == INDEPENDENT_USER) {
            items = new String[]{"CoC", "Scheller", "Culc"};
        } else {
            items = new String[]{"CoC", "Scheller", "Culc", "Tech Green", "Exhibition Hall"};
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        //Finds fields
        eventNameInput = (EditText) findViewById(R.id.eventName);
        inviteOnly = (Switch) findViewById(R.id.inviteOnly);
        eventIDInput = (EditText) findViewById(R.id.eventID);
        eventDescInput = (EditText) findViewById(R.id.eventDesc);
        eventTimemin = (EditText) findViewById(R.id.eventTimemin);
        eventTimehour = (EditText) findViewById(R.id.eventTimehour);
        eventCapacity = (EditText) findViewById(R.id.eventCapacity);
        //Create new event button functionality
        createEvent = (Button) findViewById(R.id.newEventButton);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEvent();
            }
        });

        //date picker functionality
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        eventDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        eventDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                chosenDate = new Date(year, month, day);
                String date = month + "/" + day + "/" + year;
                eventDate.setText(date);
            }
        };
    }

    public void createNewEvent() {
        eventName = eventNameInput.getText().toString();
        eventID = eventIDInput.getText().toString();
        eventDesc = eventDescInput.getText().toString();
        eventHour = Integer.parseInt(eventTimehour.getText().toString());
        eventMin = Integer.parseInt(eventTimemin.getText().toString());
        String venue = dropdown.getSelectedItem().toString();
        inviteOnlyValue = inviteOnly.isChecked();
        capacity = Integer.parseInt(eventCapacity.getText().toString());
        location = getLocation(venue);
        //Event validation
        if (eventName == null || eventName == " " || eventName.trim().isEmpty()) {
            eventNameInput.setError("Event name can't be empty");
        } else if (eventID == null || eventID == "" || eventID.trim().isEmpty()) {
            eventIDInput.setError("Event ID can't be empty");
        } else if (MainActivity.events.containsKey(eventID)) {
            eventIDInput.setError("Event ID already exists");
        } else if (eventMin < 0 || eventMin > 59) {
            eventTimemin.setError("Minute can range between 0 to 59");
        } else if (eventHour < 1 || eventHour > 23) {
            eventTimehour.setError("Hour can range between 1 to 23");
        } else if (eventDesc == null || eventDesc == "" || eventDesc.trim().isEmpty()) {
            eventDescInput.setError("Event description can't be null");
        } else if (eventCapacity == null || capacity <= 0) {
            eventCapacity.setError("Event capacity can't less than 1");
        } else if (inviteOnlyValue) {
            Intent intent = new Intent(this, AddInvitees.class);
            startActivity(intent);
        } else if (chosenDate == null) {
            eventDate.setError("Pick a date");
        } else {
            //Creating a new event based on user input
            Event newEvent = new Event(eventName, location, eventID, eventDesc, capacity,
                    inviteOnlyValue, eventHour, eventMin);
            newEvent.creator = MainActivity.currentUser;
            MainActivity.events.put(newEvent.eventName, newEvent);
            MainActivity.eventsList.add(newEvent);
            newEvent.date = chosenDate;
            Intent intent = new Intent(this, EventScreen.class);
            builder = new AlertDialog.Builder(this);
            //Success alert dialog
            builder.setTitle("New Event created")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public static Location getLocation(String venue) {
        LatLng coordinates;
        if (venue.compareTo("CoC") == 0) {
            //33.77774993918196, -84.39735242338122
            coordinates = new LatLng(33.77774993918196, -84.39735242338122);
        } else if (venue.compareTo("Scheller") == 0) {
            //33.776608257456445, -84.38771924502288
            coordinates = new LatLng(33.776608257456445, -84.38771924502288);
        } else if (venue.compareTo("Tech Green") == 0) {
            //33.77458919945886, -84.3973605585536
            coordinates = new LatLng(33.77458919945886, -84.3973605585536);
        } else if (venue.compareTo("Culc") == 0) {
            //33.77500373754406, -84.39639963920365
            coordinates = new LatLng(33.77500373754406, -84.39639963920365);
        } else { //Exhibition Hall
            //33.775020887086, -84.40180344354374
            coordinates = new LatLng(33.775020887086, -84.40180344354374);
        }
        return new Location(venue, coordinates);
    }
}
