package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Go to main activity if user is logged in
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Sign up button");
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();

                signupUser(username, password);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Login button");
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();

                loginUser(username, password);
            }
        });
    }

    private void signupUser(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "Creating user account");
        // Create the ParseUser
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.i(TAG, "Sign up user failed");
                    Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Sign up user success");
                goMainActivity();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Logging user in");
        // Log user into account using Parse backend and navigate to main activity if successful
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    // User has been successfully logged in
                    // Navigate to main activity
                    goMainActivity();
                    Log.i(TAG, "Successfully logged in");
                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed
                    Log.e(TAG, "Failed logging user in: ", e);
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}