package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.signin_signup.SQLiteHelper;
import com.example.signin_signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private SQLiteHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        dbHelper = new SQLiteHelper(this);

        // Apply animation to the login button
        Animation buttonClick = AnimationUtils.loadAnimation(this, R.anim.button_click);
        btnLogin.setOnClickListener(v -> {
            v.startAnimation(buttonClick);  // Apply animation on button click
            performLogin();
        });

        // Navigate to Sign Up screen
        tvSignUp.setOnClickListener(v -> {
            Log.d("LoginActivity", "Sign Up button clicked");
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });



        // Navigate to Forgot Password screen
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    private void performLogin() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            edtEmail.setError("Invalid email address");
            return;
        }

        // Check if the user exists in the database
        if (dbHelper.isUserValid(email, password)) {
            // Redirect to the home screen after successful login
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);  // Change this to your desired home/dashboard activity
            startActivity(intent);

            finish(); // Close login activity
        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        // Basic email validation (you can improve this later)
        return email.contains("@") && email.contains(".");
    }
}
