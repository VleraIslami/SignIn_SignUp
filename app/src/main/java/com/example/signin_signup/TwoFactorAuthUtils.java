package com.example.signin_signup;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class TwoFactorAuthUtils {

    private static final String TAG = "TwoFactorAuthUtils";

    // Generate a random 6-digit 2FA code
    public static String generate2FACode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Store the 2FA code in Firestore (with email)
    public static void store2FACode(String userId, String code, String email, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long timestamp = System.currentTimeMillis();  // Current timestamp

        // Create a new TwoFactorCode object with code, timestamp, and email
        TwoFactorCode twoFactorCode = new TwoFactorCode(code, timestamp, email);

        db.collection("twoFactorCodes").document(userId)
                .set(twoFactorCode)  // Store the 2FA code in Firestore
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "2FA code stored successfully");

                    // After storing the code, send the 2FA code via email
                    send2FACode(context, email, code);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to store 2FA code", e);
                    Toast.makeText(context, "Error storing 2FA code", Toast.LENGTH_SHORT).show();
                });
    }

    // Send the 2FA code via email (mocked here, replace with real implementation)
    public static void send2FACode(Context context, String email, String code) {
        // You can integrate Firebase Cloud Functions here which will send the email via Mailgun
        String subject = "Your 2FA Code";
        String body = "Your 2FA code is: " + code;

        // Assuming Firebase Cloud Function sends the email using Mailgun
        send2FAEmailUsingMailgun(email, subject, body);
    }

    // Mocked method to simulate email sending (replace with actual Firebase Cloud Function call)
    private static void send2FAEmailUsingMailgun(String email, String subject, String body) {
        // You would call the Firebase Cloud Function from here to send the email
        Log.d(TAG, "Email sent to " + email + " with subject: " + subject + " and body: " + body);
    }

    // Helper class for Firestore structure (optional)
    public static class TwoFactorCode {
        private String code;
        private long timestamp;
        private String email;

        public TwoFactorCode(String code, long timestamp, String email) {
            this.code = code;
            this.timestamp = timestamp;
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
