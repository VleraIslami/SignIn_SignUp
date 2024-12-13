package com.example.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        // Initialize buttons
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        Button btnGoToSignUp = findViewById(R.id.btnGoToSignUp);
        Button btnGoToAddOrEditNote = findViewById(R.id.btnGoToAddOrEditNote);

        // Navigate to LoginActivity when "Go to Login Page" button is clicked
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSuccessActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Optionally finish the current activity
        });

        // Navigate to SignUpActivity when "Go to Sign Up Page" button is clicked
        btnGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSuccessActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();  // Optionally finish the current activity
        });

        // Navigate to AddOrEditNoteActivity when "Go to Add/Edit Note" button is clicked
        btnGoToAddOrEditNote.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSuccessActivity.this, AddOrEditNoteActivity.class);
            startActivity(intent);
            finish();  // Optionally finish the current activity
        });
    }
}
