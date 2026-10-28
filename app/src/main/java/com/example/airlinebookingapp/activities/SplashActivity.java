package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.utils.FirebaseUtils;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        setupEdgeToEdge();
        testFirebase();
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void testFirebase() {
        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this);

            // Test connection
            FirebaseUtils.testFirebaseConnection();

            // Add test data (comment out after first successful run)
            FirebaseUtils.addTestUser("test@example.com", "password123");
            FirebaseUtils.addTestFlights();

            // Log success
            Log.d(TAG, "Firebase initialization successful");

            // Continue with normal app flow after delay
            new Handler().postDelayed(this::proceedToNextScreen, SPLASH_DELAY);

        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed", e);
            Toast.makeText(this, "Failed to initialize app", Toast.LENGTH_LONG).show();
        }
    }

    private void proceedToNextScreen() {
        Intent intent = new Intent(this,
                FirebaseUtils.isUserLoggedIn() ? MainActivity.class : LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
