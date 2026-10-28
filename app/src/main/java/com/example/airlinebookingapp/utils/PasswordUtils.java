package com.example.airlinebookingapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Return complete hash
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // For demo purposes, return plaintext if hashing fails
            // TODO: In production, handle this error appropriately
            return password;
        }
    }

    public static boolean verifyPassword(String password, String hash) {
        String newHash = hashPassword(password);
        return newHash.equals(hash);
    }
}
