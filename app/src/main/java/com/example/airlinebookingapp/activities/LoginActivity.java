package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.User;
import com.example.airlinebookingapp.utils.PasswordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private static final String PREF_NAME = "AirlineBookingPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            User user = db.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (user != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
                    saveLoginSession(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }
        return true;
    }

    private void saveLoginSession(User user) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_USER_ID, user.getUid())
                .putString(KEY_USER_EMAIL, user.getEmail())
                .apply();
    }
}
