package com.example.airlinebookingapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.adapters.BookingAdapter;
import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBookings;
    private TextView tvNoBookings;
    private Button btnBackToHome;
    private BookingAdapter bookingAdapter;

    private static final String PREF_NAME = "AirlineBookingPrefs";
    private static final String KEY_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_bookings);

        setupEdgeToEdge();
        initializeViews();
        setupRecyclerView();
        loadBookings();
        setupClickListeners();
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        tvNoBookings = findViewById(R.id.tvNoBookings);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }

    private void setupRecyclerView() {
        bookingAdapter = new BookingAdapter(new ArrayList<>());
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }

    private void setupClickListeners() {
        btnBackToHome.setOnClickListener(v -> finish());
    }

    private void loadBookings() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String userId = prefs.getString(KEY_USER_ID, null);

        if (userId == null) {
            Toast.makeText(this, "You are not logged in.", Toast.LENGTH_SHORT).show();
            showNoBookings();
            return;
        }

        // Corrected database call
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Booking> bookings = db.bookingDao().getBookingsByUserId(userId);

            runOnUiThread(() -> {
                if (bookings.isEmpty()) {
                    showNoBookings();
                } else {
                    showBookings(bookings);
                }
            });
        }).start();
    }

    private void showNoBookings() {
        tvNoBookings.setVisibility(View.VISIBLE);
        recyclerViewBookings.setVisibility(View.GONE);
    }

    private void showBookings(List<Booking> bookings) {
        recyclerViewBookings.setVisibility(View.VISIBLE);
        tvNoBookings.setVisibility(View.GONE);
        bookingAdapter.updateBookings(bookings);
    }
}
