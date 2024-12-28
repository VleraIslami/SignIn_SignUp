package com.example.signin_signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.security.KeyStore;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PasswordManager {

    private static final String KEY_ALIAS = "app_password_key";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    // Store app password securely using Keystore
    public static void storeAppPassword(Context context, String appPassword) {
        try {
            SecretKey secretKey = getSecretKey(context);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] iv = cipher.getIV();  // Get the Initialization Vector (IV)
            byte[] encryptedPassword = cipher.doFinal(appPassword.getBytes());

            // Combine IV and encrypted password to store together
            byte[] ivAndEncryptedPassword = new byte[iv.length + encryptedPassword.length];
            System.arraycopy(iv, 0, ivAndEncryptedPassword, 0, iv.length);
            System.arraycopy(encryptedPassword, 0, ivAndEncryptedPassword, iv.length, encryptedPassword.length);

            // Store the combined IV and encrypted password
            SharedPreferences preferences = context.getSharedPreferences("email_credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("app_password", Base64.encodeToString(ivAndEncryptedPassword, Base64.DEFAULT));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve securely stored app password
    public static String getAppPassword(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("email_credentials", Context.MODE_PRIVATE);
            String storedData = preferences.getString("app_password", null);

            if (storedData == null) {
                return null;
            }

            byte[] ivAndEncryptedPassword = Base64.decode(storedData, Base64.DEFAULT);
            byte[] iv = new byte[12];  // GCM's default IV size
            byte[] encryptedPassword = new byte[ivAndEncryptedPassword.length - iv.length];

            // Extract IV and encrypted password from the combined data
            System.arraycopy(ivAndEncryptedPassword, 0, iv, 0, iv.length);
            System.arraycopy(ivAndEncryptedPassword, iv.length, encryptedPassword, 0, encryptedPassword.length);

            SecretKey secretKey = getSecretKey(context);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decryptedPassword = cipher.doFinal(encryptedPassword);
            return new String(decryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve or generate the secret key from Keystore
    private static SecretKey getSecretKey(Context context) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);

            // Check if key already exists
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                // Generate a new key if not exists
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", KEYSTORE_PROVIDER);
                keyGenerator.init(
                        new KeyGenParameterSpec.Builder(KEY_ALIAS,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)  // Use the correct constants
                                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                .build());

                keyGenerator.generateKey();
            }

            // Retrieve the key from Keystore
            return (SecretKey) keyStore.getKey(KEY_ALIAS, null); // Now return the SecretKey instead of PublicKey
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidKeyException("Failed to retrieve the key from Keystore");
        }
    }
}
