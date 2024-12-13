package com.example.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signin_signup.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Assuming the layout name is activity_home.xml

        // Button to go to LoginActivity
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        // Button to go to AddOrEditNoteActivity
        Button btnGoToNote = findViewById(R.id.btnGoToNote);
        btnGoToNote.setOnClickListener(v -> {
            // Navigate to AddOrEditNoteActivity
            Intent noteIntent = new Intent(HomeActivity.this, AddOrEditNoteActivity.class);
            startActivity(noteIntent);
        });
    }
}
