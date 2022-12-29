package com.campusdiscovery.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import static com.campusdiscovery.app.UserType.INDEPENDENT_USER;


public class EditEvent extends AppCompatActivity {

    Button done;
    TextView eventNameEdit;
    EditText eventDescriptionEdit;
    EditText hourEdit;
    EditText minEdit;
    EditText editCapacity;

    TextView host;
    Spinner dropdown;
    Button viewAttendees;
    ArrayAdapter<String> adapter;
    Event eventToModify;
    TextView eventDate;
    DatePickerDialog.OnDateSetListener eventDateSetListener;
    Date chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        eventToModify = EventScreen.selectedEvent;
        //cannot be changed
        host = (TextView) findViewById(R.id.hostNameEdit);
        host.setText("Hosted by: " + eventToModify.creator.name);
        eventNameEdit = (TextView) findViewById(R.id.eventNameEdit);
        eventNameEdit.setText(eventToModify.eventName);
        viewAttendees = (Button) findViewById(R.id.viewAttendees);

        //editable fields
        eventDescriptionEdit = (EditText) findViewById(R.id.descEdit);
        eventDescriptionEdit.setText(eventToModify.description);
        hourEdit = (EditText) findViewById(R.id.timeHour);
        hourEdit.setText(eventToModify.hour + " ");
        minEdit = (EditText) findViewById(R.id.timeMin);
        minEdit.setText(eventToModify.min + " ");
        editCapacity = (EditText) findViewById(R.id.editCapacity);
        editCapacity.setText(Integer.toString(eventToModify.totalCapacity));
        eventDate = (TextView) findViewById(R.id.editDate);
        eventDate.setText(ExpandedEvent.formatDate(eventToModify.date));
        chosenDate = eventToModify.date;
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditEvent.this,
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
                eventDate.setText(ExpandedEvent.formatDate(chosenDate));
            }
        };

        dropdown = findViewById(R.id.locationEdit);
        String[] items;
        //sets dropdown based on logged in user type
        if (MainActivity.currentUser.type == INDEPENDENT_USER) {
            items = new String[]{"CoC", "Scheller", "Culc"};
        } else {
            items = new String[]{"CoC", "Scheller", "Culc", "Tech Green", "Exhibition Hall"};
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        //buttons
        done = findViewById(R.id.doneEdit);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEvent();
            }
        });
        viewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAttendeesOnClick();
            }
        });
    }

    public void backToEvents() {
        Intent intent = new Intent(this, EventScreen.class);
        startActivity(intent);
    }

    public void editEvent() {
        String updatedDesc = eventDescriptionEdit.getText().toString();
        int updatedHour = Integer.parseInt(hourEdit.getText().toString().trim());
        int updatedMin = Integer.parseInt(minEdit.getText().toString().trim());
        String updatedLocation = dropdown.getSelectedItem().toString();
        Location location = CreateEvent.getLocation(updatedLocation);
        int capacityInt = Integer.parseInt(editCapacity.getText().toString());
        if (updatedHour < 1 || updatedHour > 23) {
            hourEdit.setError("Hour can range from 1 to 23");
        } else if (updatedMin < 0 || updatedMin > 59) {
            minEdit.setError("Minute can range from 0 to 59");
        } else if (updatedDesc == null || updatedDesc == "" || updatedDesc.trim().isEmpty()) {
            eventDescriptionEdit.setError("Event description can't be null");
        } else if (editCapacity == null || capacityInt <= 0) {
            editCapacity.setError("Enter a valid capacity");
        }
        else {
            //modify event attributes
            MainActivity.eventsList.remove(eventToModify);
            eventToModify.description = updatedDesc;
            eventToModify.min = updatedMin;
            eventToModify.hour = updatedHour;
            eventToModify.location = location;
            eventToModify.totalCapacity = capacityInt;
            eventToModify.date = chosenDate;
            MainActivity.events.put(eventToModify.eventName, eventToModify);
            MainActivity.eventsList.add(eventToModify);
            backToEvents();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewAttendeesOnClick() {
        Intent intent = new Intent(this, ViewAttendees.class);
        startActivity(intent);
    }
}