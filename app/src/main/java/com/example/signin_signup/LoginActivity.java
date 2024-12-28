package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnForgotPassword;
    private TextView tvSignUp, tvForgotPassword;
    private CheckBox checkboxShowPassword;

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
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        checkboxShowPassword = findViewById(R.id.checkboxShowPassword);

        // Apply animation to the login button
        Animation buttonClick = AnimationUtils.loadAnimation(this, R.anim.button_click);
        btnLogin.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
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

        // Show/hide password logic
        checkboxShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            edtPassword.setSelection(edtPassword.length());
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

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Trigger 2FA code sending after login
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        send2FACode(userId, email);

                        // Navigate to the verification activity
                        Intent intent = new Intent(LoginActivity.this, Verify2FACodeActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        // Sign-in failed
                        String error = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void send2FACode(String userId, String email) {
        // Validate the email before proceeding
        if (email == null || email.trim().isEmpty()) {
            Log.e("send2FACode", "Email is invalid or empty.");
            return;
        }

        String code = generate2FACode();
        store2FACode(userId, code);

        String subject = "Your 2FA Code";
        String body = "Your 2FA code is: " + code;

        // Send the email only if email is valid
        EmailSender.sendEmail(getApplicationContext(), email, subject, body);
    }




    private String generate2FACode() {
        Random rand = new Random();
        int code = rand.nextInt(999999);  // Generate a random 6-digit code
        return String.format("%06d", code); // Format to 6 digits
    }

    private void sendEmail(String email, String subject, String message) {
        // Use Firebase Cloud Functions or any email service (e.g., SendGrid) to send the email
        // For simplicity, assume it's handled via your backend.
        Log.d("2FA", "Sending 2FA code to: " + email);
    }

    private void store2FACode(String userId, String code) {
        // Store the generated 2FA code in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);  // Store the generated 2FA code
        data.put("userId", userId);  // Store the user ID

        // Write to Firestore
        db.collection("twoFactorCodes").document(userId).set(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "2FA code successfully written to Firestore for userId: " + userId);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error writing 2FA code to Firestore", e);
                });
    }

    private void sendPasswordResetLink(String email) {
        // Configure the ActionCodeSettings with your Firebase Hosting domain
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://signupsignin-306bf.web.app/__/auth/action")
                .setHandleCodeInApp(true)
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
