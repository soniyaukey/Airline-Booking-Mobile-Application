package com.example.airlinebookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Correctly initialize class member UI elements
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // --- Input Validation ---
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full Name is required.");
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address.");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required.");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters long.");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match.");
            etConfirmPassword.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Creating Account...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Register");

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // --- Save user to local database ---
                            User newUser = new User(fullName, email, password);
                            newUser.setUid(uid); // IMPORTANT: Set the Firebase UID
                            AppDatabase db = AppDatabase.getInstance(RegisterActivity.this);
                            new Thread(() -> db.userDao().insert(newUser)).start();
                            // -------------------------------------

                            // Update user's Firebase profile (optional but good practice)
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            firebaseUser.updateProfile(profileUpdates);
                        }

                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                        // Redirect to Login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
