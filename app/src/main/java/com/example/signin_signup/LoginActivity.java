package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin,btnForgotPassword;
    private TextView tvSignUp, tvForgotPassword;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);  // Initialize the button

        // Apply animation to the login button
        Animation buttonClick = AnimationUtils.loadAnimation(this, R.anim.button_click);
        btnLogin.setOnClickListener(v -> {
            v.startAnimation(buttonClick);  // Apply animation on button click
            performLogin();
        });

        // Navigate to Sign Up screen
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Forgot Password functionality
        tvForgotPassword.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            // Check if the email field is empty
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return; // Prevent proceeding if email is empty
            }

            // Check if the email entered is valid
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return; // Prevent proceeding if email is invalid
            }

            // Call function to send password reset link
            sendPasswordResetLink(email);
        });

        btnForgotPassword.setOnClickListener(v -> {
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

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email address");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to LoginSuccessActivity after successful login
                        Intent intent = new Intent(LoginActivity.this, LoginSuccessActivity.class);
                        startActivity(intent);
                        finish();  // Close the LoginActivity
                    } else {
                        // Sign-in failed
                        String error = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void sendPasswordResetLink(String email) {


        // Configure the ActionCodeSettings with your Firebase Hosting domain
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://signupsignin-306bf.web.app/__/auth/action")
                .setHandleCodeInApp(true)
                //.setDynamicLinkDomain("https://signupsignin-306bf.web.app/reset")
                .build();


        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        Log.d("ForgotPassword", "Reset email sent to: " + email);
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(LoginActivity.this, "Failed to send reset email: " + error, Toast.LENGTH_SHORT).show();
                        Log.e("ForgotPassword", "Error: " + error);
                    }
                });
    }

}