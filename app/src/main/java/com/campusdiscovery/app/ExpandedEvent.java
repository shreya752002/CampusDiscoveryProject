package com.campusdiscovery.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;


public class ExpandedEvent extends AppCompatActivity {
    Button editEvent;
    Button deleteEvent;
    Button rsvpButton;
    TextView eventName;
    TextView eventDescription;
    TextView eventLocation;
    TextView eventTime;
    TextView host;
    TextView eventCapacity;
    TextView numOfAttendees;
    TextView eventDate;
    Spinner rsvp;
    ArrayAdapter<String> adapter;
    AlertDialog.Builder builder;
    User currentUser;
    Event currentEvent;
    //to check which page to go back to
    static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editEvent = (Button) findViewById(R.id.editEvent);
        deleteEvent = (Button) findViewById(R.id.deleteEvent);
        eventName = (TextView) findViewById(R.id.eventNameExt);
        eventDescription = (TextView) findViewById(R.id.eventDescExt);
        eventLocation = (TextView) findViewById(R.id.eventLocationExt);
        eventCapacity = (TextView) findViewById(R.id.capacity);
        eventTime = (TextView) findViewById(R.id.eventTimeExt);
        host = (TextView) findViewById(R.id.host);
        eventDate = (TextView) findViewById(R.id.dateDisplay);
        numOfAttendees = (TextView) findViewById(R.id.currentCapacity);
        rsvpButton = (Button) findViewById(R.id.RSVPButton);
        rsvp = (Spinner) findViewById(R.id.rsvp);

        currentUser = MainActivity.currentUser;
        currentEvent = EventScreen.selectedEvent;

        String rsvpOptions[] = {"Will attend", "May attend",
                "Will not attend", "I'm your nemesis"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                rsvpOptions);
        rsvp.setAdapter(adapter);
        Event displayEvent = EventScreen.selectedEvent;
        eventName.setText(displayEvent.eventName);
        eventDescription.setText("Description: " + displayEvent.description);
        eventLocation.setText("Location: " + displayEvent.location.location);
        eventTime.setText("Time: " + displayEvent.hour + ":" + displayEvent.min);
        eventCapacity.setText("Capacity: " + displayEvent.totalCapacity);
        numOfAttendees.setText("Number of Attendees: " + displayEvent.currentNumAttendees);
        host.setText("Hosted by: " + displayEvent.creator.name);
        eventDate.setText("Date: " + formatDate(displayEvent.date));
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEvent();
            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent();
            }
        });

        rsvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsvpForEvent();
            }
        });
    }

    public void backEventScreen() {
        Intent intent = new Intent(this, EventScreen.class);
        startActivity(intent);
    }

    public void editEvent() {
        if (currentEvent.creator.uniqueId.compareTo(currentUser.uniqueId) == 0
                || currentUser.type == UserType.ADMIN) {
            Intent intent = new Intent(this, EditEvent.class);
            startActivity(intent);
        } else {
            alertBuilder("Error", "Cannot edit events you didn't create.");
        }
    }

    public void deleteEvent() {
        if (currentEvent.creator.uniqueId.compareTo(currentUser.uniqueId) == 0
                || currentUser.type == UserType.ADMIN) {
            MainActivity.events.remove(currentEvent.eventName);
            MainActivity.eventsList.remove(currentEvent);
            backEventScreen();
        } else {
            alertBuilder("Error", "Cannot delete events you didn't create.");
        }
    }

    public void rsvpForEvent() {
        String choice = rsvp.getSelectedItem().toString();
        RsvpType userRsvp;
        if (choice.compareTo("Will attend") == 0) {
            userRsvp = RsvpType.WILL_ATTEND;
        } else if (choice.compareTo("May attend") == 0) {
            userRsvp = RsvpType.MAYBE;
        } else if (choice.compareTo("Will not attend") == 0) {
            userRsvp = RsvpType.WILL_NOT_ATTEND;
        } else {
            userRsvp = RsvpType.NEMESIS;
        }
        if (currentUser.type != UserType.INDEPENDENT_USER) {
            alertBuilder("Error", "Only independent users can RSVP to events.");
        } else if (currentUser.uniqueId == currentEvent.creator.uniqueId) {
            alertBuilder("Error", "Event creators cannot RSVP.");
        } else if (currentEvent.inviteOnly && !currentEvent.inviteeList.contains(currentUser.uniqueId)) {
            alertBuilder("Error", "This is an Invite Only event");
        } else {
            //removes previous rsvp
            RsvpType oldRsvp = checkRSVP();
            boolean flag = false;
            switch (userRsvp) {
                case WILL_ATTEND:
                    if (oldRsvp != null && oldRsvp.compareTo(RsvpType.WILL_ATTEND) == 0) {
                        alertBuilder("Error", "You've already RSVPed to attend this event");
                        currentEvent.willAttend.add(currentUser.uniqueId);
                        currentEvent.currentNumAttendees++;
                    } else {
                        Event conflict = timeConflict();
                        if (conflict != null) {
                            flag = true;
                            alertBuilder("Time conflict", "Time conflict with " + conflict.eventName);
                        }
                        else if (currentEvent.currentNumAttendees < currentEvent.totalCapacity) {
                            currentEvent.willAttend.add(currentUser.uniqueId);
                            currentEvent.currentNumAttendees++;
                            alertBuilder("Success", "You will attend the event.");
                        } else {
                            rsvpButton.setError("Maximum capacity reached");
                        }
                    }
                    break;
                case WILL_NOT_ATTEND:
                    if (oldRsvp != null && oldRsvp.compareTo(RsvpType.WILL_NOT_ATTEND) == 0) {
                        alertBuilder("Error", "You've already RSVPed to not attend this event");
                        currentEvent.willNotAttend.add(currentUser.uniqueId);
                    } else {
                        currentEvent.willNotAttend.add(currentUser.uniqueId);
                        alertBuilder("Success", "You will not attend the event.");
                    }
                    break;
                case MAYBE:
                    if (oldRsvp != null && oldRsvp.compareTo(RsvpType.MAYBE) == 0) {
                        alertBuilder("Error", "You've already RSVPed to maybe attend this event");
                        currentEvent.maybeAttend.add(currentUser.uniqueId);
                    } else {
                        currentEvent.maybeAttend.add(currentUser.uniqueId);
                        alertBuilder("Success", "You may attend the event.");
                    }
                    break;
                default:
                    if (oldRsvp != null && oldRsvp.compareTo(RsvpType.NEMESIS) == 0) {
                        alertBuilder("Error", "You've already a nemesis");
                        currentEvent.nemesisUsers.add(currentUser.uniqueId);
                    } else {
                        currentEvent.nemesisUsers.add(currentUser.uniqueId);
                        alertBuilder("Success", "You are the creator's nemesis.");
                    }
                    break;
            }
            if (!flag) {
                currentUser.rsvpStatus.put(currentEvent.eventName, userRsvp);
            }
        }
    }

    private Event timeConflict() {
        for (String eventName : currentUser.rsvpStatus.keySet()) {
            Event event = MainActivity.events.get(eventName);
            if (currentUser.rsvpStatus.get(eventName).compareTo(RsvpType.WILL_ATTEND) == 0 && event.date.compareTo(currentEvent.date) == 0) {
                if (event.hour == currentEvent.hour && event.min == currentEvent.min) {
                    return event;
                }
            }
        }
        return null;
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

    private RsvpType checkRSVP() {
        if (currentUser.rsvpStatus.containsKey(currentEvent.eventId)) {
            //extract old rsvp
            RsvpType oldRSVP = currentUser.rsvpStatus.get(currentEvent.eventId);
            if (oldRSVP == RsvpType.MAYBE) {
                currentEvent.maybeAttend.remove(currentUser.uniqueId);
            } else if (oldRSVP == RsvpType.NEMESIS) {
                currentEvent.nemesisUsers.remove(currentUser.uniqueId);
            } else if (oldRSVP == RsvpType.WILL_ATTEND) {
                currentEvent.willAttend.remove(currentUser.uniqueId);
                currentEvent.currentNumAttendees--;
            } else {
                currentEvent.willNotAttend.remove(currentUser.uniqueId);
            }
            currentUser.rsvpStatus.remove(currentEvent.eventId);
            return oldRSVP;
        } else {
            return null;
        }
    }

    private void alertBuilder(String title, String message) {
        Intent intent;
        if (!flag) {
            intent = new Intent(this, EventScreen.class);
        } else {
            intent = new Intent(this, RsvpEvents.class);
        }
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(intent);
                    }
                })
                .show();
    }

    public static String formatDate(Date date) {
        String dateString = "";
        String month = "";
        switch(date.getMonth()) {
            case 00: {
                month = "JAN";
                break;
            }
            case 1: {
                month = "FEB";
                break;
            }
            case 2: {
                month = "MAR";
                break;
            }
            case 3: {
                month = "APR";
                break;
            }
            case 4: {
                month = "MAY";
                break;
            }
            case 5: {
                month = "JUN";
                break;
            }
            case 6: {
                month = "JULY";
                break;
            }
            case 7: {
                month = "AUG";
                break;
            }
            case 8: {
                month = "SEPT";
                break;
            }
            case 9: {
                month = "OCT";
                break;
            }
            case 10: {
                month = "NOV";
                break;
            }
            case 11: {
                month = "DEC";
                break;
            }
            default: {
                month = "JAN";
            }
        }
        dateString = date.getDate() + " " + month + " " + date.getYear();
        return dateString;
    }
}
