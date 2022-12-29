package com.campusdiscovery.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class AddInvitees extends AppCompatActivity {

    EditText addInvitees;
    Button addInviteesButton;
    Button doneInviteesButton;
    HashSet<String> inviteeList;
    AlertDialog.Builder doneBuilder;
    AlertDialog.Builder addBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitees);
        addInvitees = (EditText) findViewById(R.id.inviteUserId);
        addInviteesButton = (Button) findViewById(R.id.addInvitees);
        doneInviteesButton = (Button) findViewById(R.id.doneInvitees);
        inviteeList = new HashSet<>();
        addInviteesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        doneInviteesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneInvites();
            }
        });
    }
    public void addUser() {
        String userId = addInvitees.getText().toString();
        if (userId == null || userId.trim() == "") {
            addInvitees.setError("Enter a valid User ID");
        } else if (!MainActivity.users.containsKey(userId)) {
            addInvitees.setError("User ID doesn't exist");
        } else if (inviteeList.contains(userId)) {
            addInvitees.setError("This user is already in the invitee list!");
        } else {
            inviteeList.add(userId);
            addBuilder = new AlertDialog.Builder(this);
            addBuilder.setTitle("Sent an invite!")
                    .show();
        }
    }
    public void doneInvites() {
        Event newEvent = new Event(CreateEvent.eventName, CreateEvent.location, CreateEvent.eventID,
                 CreateEvent.eventDesc, CreateEvent.capacity, CreateEvent.inviteOnlyValue, CreateEvent.eventHour, CreateEvent.eventMin);
        newEvent.inviteeList = inviteeList;
        newEvent.creator = MainActivity.currentUser;
        MainActivity.events.put(newEvent.eventName, newEvent);
        MainActivity.eventsList.add(newEvent);
        Intent intent = new Intent(this, EventScreen.class);
        doneBuilder = new AlertDialog.Builder(this);
        //Success alert dialog
        doneBuilder.setTitle("New Event created")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(intent);
                    }
                }).show();
    }

}