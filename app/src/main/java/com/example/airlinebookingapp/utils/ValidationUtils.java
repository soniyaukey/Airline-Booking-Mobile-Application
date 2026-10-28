package com.example.airlinebookingapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && name.trim().length() >= 2;
    }

    public static boolean isValidPassengerCount(int count) {
        return count > 0 && count <= 6;
    }

    public static boolean isValidDate(String date) {
        // Basic date validation - you might want to add more sophisticated validation
        return !TextUtils.isEmpty(date) && date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
}
