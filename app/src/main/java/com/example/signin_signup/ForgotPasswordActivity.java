package com.example.signin_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views and Firebase Auth
        edtEmail = findViewById(R.id.edtEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();

        // Handle reset password button click
        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            // Check if email is empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Send password reset email using Firebase
            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_SHORT).show();
                        // Optionally, redirect to login screen after sending the reset link
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close the ForgotPasswordActivity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
