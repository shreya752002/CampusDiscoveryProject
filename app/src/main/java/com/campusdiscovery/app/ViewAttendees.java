package com.campusdiscovery.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendees extends AppCompatActivity {

    Spinner willAttend;
    Spinner willNotAttend;
    Spinner maybeAttend;
    Spinner nemesis;
    Button back;
    ArrayAdapter<String> willAttendAdapter;
    ArrayAdapter<String> willNotAttendAdapter;
    ArrayAdapter<String> maybeAdapter;
    ArrayAdapter<String> nemesisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendees);
        willAttend = (Spinner) findViewById(R.id.willAttendSpinner);
        willNotAttend = (Spinner) findViewById(R.id.willNotAttendSpinner);
        maybeAttend = (Spinner) findViewById(R.id.maybeAttendSpinner);
        nemesis = (Spinner) findViewById(R.id.nemesisSpinner);
        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButton();
            }
        });

        willAttendAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, convertList(EventScreen.selectedEvent.willAttend));
        willAttend.setAdapter(willAttendAdapter);

        willAttend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                confirmUser(willAttend.getSelectedItem().toString(), EventScreen.selectedEvent.willAttend, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        willNotAttendAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, convertList(EventScreen.selectedEvent.willNotAttend));
        willNotAttend.setAdapter(willNotAttendAdapter);

        willNotAttend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                confirmUser(willNotAttend.getSelectedItem().toString(), EventScreen.selectedEvent.willNotAttend, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        maybeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, convertList(EventScreen.selectedEvent.maybeAttend));
        maybeAttend.setAdapter(maybeAdapter);

        maybeAttend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                confirmUser(maybeAttend.getSelectedItem().toString(), EventScreen.selectedEvent.maybeAttend, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nemesisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, convertList(EventScreen.selectedEvent.nemesisUsers));
        nemesis.setAdapter(nemesisAdapter);

        nemesis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                confirmUser(nemesis.getSelectedItem().toString(), EventScreen.selectedEvent.nemesisUsers, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void confirmUser(String selectedUser, List<String> currentList, boolean willAttend) {
        if (selectedUser.compareTo("--") != 0) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("ReCheck")
                    .setMessage("Are you sure you want to remove " + selectedUser)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.users.get(selectedUser).rsvpStatus.remove(EventScreen.selectedEvent);
                            currentList.remove(selectedUser);
                            if (willAttend) {
                                EventScreen.selectedEvent.currentNumAttendees--;
                            }
                            declareIntent();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            declareIntent();
                        }
                    })
                    .show();
        }
    }

    public void declareIntent() {
        Intent intent = new Intent(this, ViewAttendees.class);
        startActivity(intent);
    }

    public List<String> convertList(List<String> currentList) {
        List<String> changedList = new ArrayList<>(currentList);
        changedList.add(0, "--");
        return changedList;
    }

    public void backButton() {
        Intent intent = new Intent(this, EditEvent.class);
        startActivity(intent);
    }
}