package com.example.signin_signup;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.security.Signature;

public class KeystoreUtils {
    private static final String KEY_ALIAS = "app_password_key";  // The key alias for encryption/decryption

    // Initialize Keystore
    public static KeyStore getKeystoreInstance() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);  // Load the keystore
        return keyStore;
    }

    // Generate a new key or retrieve an existing key for encryption
    public static void createKey() throws Exception {
        KeyStore keyStore = getKeystoreInstance();
        if (keyStore.containsAlias(KEY_ALIAS)) {
            return;  // Key already exists
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(
                new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
        );
        keyGenerator.generateKey();
    }

    // Encrypt data using the Keystore
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKey key = (SecretKey) getKeystoreInstance().getKey(KEY_ALIAS, null);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV();
        byte[] encryption = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryption, Base64.DEFAULT) + ":" + Base64.encodeToString(iv, Base64.DEFAULT);
    }

    // Decrypt data using the Keystore
    public static String decrypt(String encryptedData) throws Exception {
        String[] parts = encryptedData.split(":");
        byte[] encryptedBytes = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] iv = Base64.decode(parts[1], Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKey key = (SecretKey) getKeystoreInstance().getKey(KEY_ALIAS, null);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted);
    }
}
