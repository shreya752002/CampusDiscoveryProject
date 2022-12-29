package com.campusdiscovery.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/*
Independent User = 0
RSO = 1
Admin = 2
 */
public class SignUp extends AppCompatActivity {
    Button submitButton;
    EditText nameInput;
    EditText passwordInput;
    EditText userIdInput;
    RadioGroup userTypeRadio;
    RadioButton selectedUserType;
    String signUpName;
    String signUpPassword;
    String signUpUserId;
    int userType;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Finds fields
        nameInput = (EditText) findViewById(R.id.signUpName);
        passwordInput = (EditText) findViewById(R.id.passwordSignUp);
        userIdInput = (EditText) findViewById(R.id.userIdSignUp);
        userTypeRadio = (RadioGroup) findViewById(R.id.radioGroup);
        //Button listener
        submitButton = (Button) findViewById(R.id.buttonSignUp);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPressed();
            }
        });
    }
    //Checks for valid submissions, creates new user, adds user to hashmap
    public void submitPressed() {
        signUpName = nameInput.getText().toString();
        signUpPassword = passwordInput.getText().toString();
        signUpUserId = userIdInput.getText().toString();
        userType = userTypeRadio.getCheckedRadioButtonId();
        selectedUserType = findViewById(userType);
        //validation
        if (signUpName == null || signUpName == "" || signUpName.trim().isEmpty()) {
            nameInput.setError("Name can't be null, empty, or white spaces");
        } else if (signUpPassword == null || signUpPassword == "" || signUpPassword.trim().isEmpty()) {
            passwordInput.setError("Password can't be null, empty, or white spaces");
        } else if (signUpUserId == null || signUpUserId == "" || signUpUserId.trim().isEmpty()) {
            userIdInput.setError("UserId can't be null, empty, or white spaces");
        } else if (MainActivity.users.containsKey(signUpUserId)) {
            userIdInput.setError("UserId already exists");
        } else {
            String userType = selectedUserType.getText().toString();
            UserType uType;
            if (userType.compareTo("Independent User") == 0) {
                uType = UserType.INDEPENDENT_USER;
            } else if (userType.compareTo("RSO") == 0) {
                uType = UserType.RSO;
            } else {
                uType = UserType.ADMIN;
            }

            User user = new User(signUpName, signUpPassword, uType, signUpUserId);
            Log.d(user.type.toString(), "new user");
            Intent intent = new Intent(this, EventScreen.class);
            MainActivity.users.put(signUpUserId, user);
            MainActivity.currentUser = user;
            //shows success if all criterias match
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Success")
                    .setMessage("Welcome " + signUpName + ". You're registered as a " + userType)
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(intent);
                        }
                    }).show();
        }
    }
}