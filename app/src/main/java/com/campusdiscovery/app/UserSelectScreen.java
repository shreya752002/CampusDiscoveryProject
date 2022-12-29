package com.campusdiscovery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class UserSelectScreen extends AppCompatActivity {
    String nameInput;
    EditText passEdit;
    String password;
    EditText name;
    Button signUpButton;
    Button loginSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select_screen);

        name = (EditText) findViewById(R.id.loginName);
        passEdit = (EditText) findViewById(R.id.loginPass);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
        loginSubmit = (Button) findViewById(R.id.loginButton);
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSubmit();
            }
        });
    }
    public void onClickSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    public void userSubmit() {
        nameInput = name.getText().toString();
        password = passEdit.getText().toString();
        if (nameInput == null || nameInput == "" || nameInput.trim().isEmpty()) {
            name.setError("Name can't be null or empty or white spaces");
        } else if (password == null || password == "" || password.trim().isEmpty()) {
            passEdit.setError("Password can't be null or empty or white spaces");
        } else {
            Intent intent = new Intent(this, EventScreen.class);
            if (MainActivity.users.containsKey(nameInput)) {
                if (MainActivity.users.get(nameInput).password.compareTo(password) == 0) {
                    MainActivity.currentUser = MainActivity.users.get(nameInput);
                    startActivity(intent);
                } else {
                    passEdit.setError("Password Incorrect");
                }
            } else {
                name.setError("User does not exist");
            }
        }
    }
}