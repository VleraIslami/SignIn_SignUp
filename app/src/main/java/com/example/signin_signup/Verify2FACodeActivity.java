package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class Verify2FACodeActivity extends AppCompatActivity {

    private EditText edt2FACode;
    private Button btnVerifyCode;
    private FirebaseFirestore db;
    private String userId;
    private String stored2FACode;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_2fa_code);

        // Initialize UI elements
        edt2FACode = findViewById(R.id.edt2FACode);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");

        // Show loading progress while fetching 2FA code
        progressBar.setVisibility(View.VISIBLE);
        btnVerifyCode.setEnabled(false);

        // Fetch the 2FA code from Firestore
        fetchStored2FACode();

        // Handle the button click for verification
        btnVerifyCode.setOnClickListener(v -> verify2FACode());
    }

    private void fetchStored2FACode() {
        // Fetch the 2FA code from Firestore for the given user ID
        db.collection("twoFactorCodes").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE); // Hide loading spinner

                    if (documentSnapshot.exists()) {
                        // Retrieve the code from the document
                        stored2FACode = documentSnapshot.getString("code");
                        Log.d("Firestore", "Fetched 2FA code: " + stored2FACode);

                        // Enable the verify button after fetching the code
                        btnVerifyCode.setEnabled(true);
                    } else {
                        Toast.makeText(Verify2FACodeActivity.this, "No code found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // Hide loading spinner on error
                    Toast.makeText(Verify2FACodeActivity.this, "Error fetching code", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error fetching 2FA code from Firestore", e);
                });
    }

    private void verify2FACode() {
        // Get the entered code from the user
        String enteredCode = edt2FACode.getText().toString().trim();

        if (TextUtils.isEmpty(enteredCode)) {
            edt2FACode.setError("Please enter the 2FA code");
            return;
        }

        // Compare the entered code with the stored code
        if (enteredCode.equals(stored2FACode)) {
            // Code is correct, show success message
            Toast.makeText(Verify2FACodeActivity.this, "2FA Verified!", Toast.LENGTH_SHORT).show();

            // Delete the 2FA code from Firestore to prevent reuse
            delete2FACodeFromFirestore();

            // Proceed to the main activity
            Intent intent = new Intent(Verify2FACodeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Code doesn't match, show error message
            Toast.makeText(Verify2FACodeActivity.this, "Invalid 2FA code. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete2FACodeFromFirestore() {
        // Delete the 2FA code from Firestore to prevent reuse
        db.collection("twoFactorCodes").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "2FA code successfully deleted"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting 2FA code", e));
    }
}
