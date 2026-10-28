package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private Button btnChangePhoto;
    private Button btnUpdateProfile;
    private Button btnLogout;
    private ImageView imgProfile;

    private static final String PREF_NAME = "AirlineBookingPrefs";
    private static final String KEY_USER_EMAIL = "userEmail";
    private User currentUser;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        loadUserProfile();
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        etPhone = findViewById(R.id.etProfilePhone);
        etAddress = findViewById(R.id.etProfileAddress);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);
        imgProfile = findViewById(R.id.imgProfile);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String email = prefs.getString(KEY_USER_EMAIL, null);

        if (email == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            currentUser = db.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (currentUser != null) {
                    etName.setText(currentUser.getName());
                    etEmail.setText(currentUser.getEmail());
                }
            });
        });
    }

    private void setupClickListeners() {
        btnChangePhoto.setOnClickListener(v -> handleChangePhoto());
        btnUpdateProfile.setOnClickListener(v -> handleUpdate());
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void handleChangePhoto() {
        Toast.makeText(this, "Change Photo functionality not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void handleUpdate() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (!validateInputs(name, email)) {
            return;
        }

        currentUser.setName(name);
        currentUser.setEmail(email);

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.userDao().update(currentUser);

            runOnUiThread(() -> {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                prefs.edit().putString(KEY_USER_EMAIL, email).apply();
                finish();
            });
        });
    }

    private boolean validateInputs(String name, String email) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return false;
        }
        return true;
    }

    private void handleLogout() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
