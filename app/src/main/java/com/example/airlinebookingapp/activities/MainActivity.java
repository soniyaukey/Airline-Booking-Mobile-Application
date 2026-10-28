package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.adapters.BookingAdapter;
import com.example.airlinebookingapp.models.Booking;
import com.example.airlinebookingapp.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnSearchFlights;
    private RecyclerView rvBookings;
    private TextView tvWelcome;
    private BookingAdapter bookingAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private static final String PREF_NAME = "AirlineBookingPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeFirebase();
        setupEdgeToEdge();
        initializeViews();
        setupRecyclerView();
        loadUserBookings();

        // Initialize sample flights (only for first time setup)
        // Comment this out after first run
        FirebaseUtils.addSampleFlights();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        btnSearchFlights = findViewById(R.id.btnSearchFlights);
        rvBookings = findViewById(R.id.rvBookings);
        tvWelcome = findViewById(R.id.tvWelcome);

        String userEmail = firebaseAuth.getCurrentUser() != null ?
                firebaseAuth.getCurrentUser().getEmail() : "";
        tvWelcome.setText("Welcome, " + userEmail);

        btnSearchFlights.setOnClickListener(v ->
                startActivity(new Intent(this, SearchFlightActivity.class)));
    }

    private void setupRecyclerView() {
        bookingAdapter = new BookingAdapter(new ArrayList<>());
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setAdapter(bookingAdapter);
    }

    private void loadUserBookings() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            firestore.collection("bookings")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Booking> bookings = new ArrayList<>();
                        queryDocumentSnapshots.forEach(doc -> {
                            Booking booking = doc.toObject(Booking.class);
                            booking.setBookingId(doc.getId());
                            bookings.add(booking);
                        });
                        bookingAdapter.updateBookings(bookings);
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            handleLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        // Clear session
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserBookings(); // Refresh bookings when returning to screen
    }
}
