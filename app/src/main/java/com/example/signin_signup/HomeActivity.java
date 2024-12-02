package com.example.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signin_signup.LoginActivity;
import com.example.signin_signup.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnLogout;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.btnLogout);
        tvWelcome = findViewById(R.id.tvWelcome);

        tvWelcome.setText("Welcome to Home!");

        btnLogout.setOnClickListener(v -> {
            // Log out logic (navigate to login)
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
