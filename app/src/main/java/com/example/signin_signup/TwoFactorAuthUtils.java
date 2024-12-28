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

    // Store the 2FA code in Firestore
    public static void store2FACode(String userId, String code, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("twoFactorCodes").document(userId)
                .set(new TwoFactorCode(code))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "2FA code stored successfully"))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to store 2FA code", e);
                    Toast.makeText(context, "Error storing 2FA code", Toast.LENGTH_SHORT).show();
                });
    }

    // Send the 2FA code via email (mocked here, replace with real implementation)
    public static void send2FACode(Context context, String email, String code) {
        // Mock sending the code
        Log.d(TAG, "2FA code sent to " + email + ": " + code);
        Toast.makeText(context, "2FA code sent to " + email, Toast.LENGTH_SHORT).show();
    }

    // Helper class for Firestore structure (optional)
    public static class TwoFactorCode {
        private String code;

        public TwoFactorCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
