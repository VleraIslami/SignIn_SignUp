package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText edtForgotPasswordEmail;
    private MaterialButton btnReset, btnBack;
    private ProgressBar forgetPasswordProgressbar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        edtForgotPasswordEmail = findViewById(R.id.edtForgotPasswordEmail);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnForgotPasswordBack);
        forgetPasswordProgressbar = findViewById(R.id.forgetPasswordProgressbar);

        // Reset Password button click listener
        btnReset.setOnClickListener(v -> {
            String email = edtForgotPasswordEmail.getText().toString().trim();

            // Validate email
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email);
        });

        // Back button click listener
        btnBack.setOnClickListener(v -> finish());
    }

    private void resetPassword(String email) {
        // Show progress bar while sending the reset email
        forgetPasswordProgressbar.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        // Send password reset email using Firebase
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish();  // Close ForgotPasswordActivity
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Failed to send reset email";
                        Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    // Hide progress bar and show button again
                    forgetPasswordProgressbar.setVisibility(View.INVISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                });
    }
}
