package com.example.airlinebookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // 1. Declare UI elements
    private TextView tvUserName;
    private Button btnLogout;

    // 2. Declare Firebase Auth instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3. Initialize UI elements
        tvUserName = findViewById(R.id.tvUserName);
        btnLogout = findViewById(R.id.btnLogout);

        // 4. Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // 5. Check if a user is currently logged in
        if (currentUser == null) {
            // If no user is logged in, redirect to LoginActivity
            redirectToLogin();
            return; // Stop further execution in onCreate
        }

        // 6. Get user's display name and set it to the TextView
        String userName = currentUser.getDisplayName();
        if (userName != null && !userName.isEmpty()) {
            tvUserName.setText(userName);
        } else {
            // If the display name is not set, use their email as a fallback
            tvUserName.setText(currentUser.getEmail());
        }

        // 7. Set up the click listener for the Logout button
        btnLogout.setOnClickListener(v -> {
            // Sign out the user from Firebase
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            // Redirect to the LoginActivity after logging out
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        // Clear all previous activities from the stack to prevent the user from going back
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish MainActivity
    }
}
