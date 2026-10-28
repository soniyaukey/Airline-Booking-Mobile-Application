package com.example.airlinebookingapp.activities;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> handleRegistration());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void handleRegistration() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        String hashedPassword = PasswordUtils.hashPassword(password);
                        User newUser = new User(name, email, hashedPassword);
                        newUser.setUid(uid);

                        executorService.execute(() -> {
                            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                            db.userDao().insert(newUser);

                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            });
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return false;
        }
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
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
