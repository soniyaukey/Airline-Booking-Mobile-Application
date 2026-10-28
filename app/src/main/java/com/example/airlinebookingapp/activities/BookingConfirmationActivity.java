package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlinebookingapp.databinding.ActivityBookingConfirmationBinding;
import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.Booking;
import com.example.airlinebookingapp.models.Flight;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingConfirmationActivity extends AppCompatActivity {

    private ActivityBookingConfirmationBinding binding;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBookingDetails();
        setupClickListeners();
    }

    private void loadBookingDetails() {
        String bookingId = getIntent().getStringExtra("bookingId");
        if (bookingId == null || bookingId.isEmpty()) {
            Toast.makeText(this, "Invalid booking ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Booking booking = db.bookingDao().getBookingById(bookingId);

            if (booking == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Booking details not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            Flight flight = db.flightDao().getFlightByNumber(booking.getFlightId());

            runOnUiThread(() -> {
                if (flight != null) {
                    displayBookingDetails(booking, flight);
                } else {
                    Toast.makeText(this, "Could not load complete booking details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void displayBookingDetails(Booking booking, Flight flight) {
        binding.tvBookingId.setText(String.format("Booking ID: %s", booking.getBookingId()));
        binding.tvFlightName.setText("Airline: " + flight.getAirline());
        binding.tvFlightRoute.setText(String.format("From: %s To: %s", flight.getDeparture(), flight.getDestination()));
        binding.tvFlightTime.setText(String.format("Departure: %s | Arrival: %s", flight.getDepartureTime(), flight.getArrivalTime()));

        binding.tvPassengerName.setText("Passenger: " + booking.getPassengerNames());

        binding.tvSeatClass.setVisibility(View.GONE);

        binding.tvTotalAmount.setText(String.format(Locale.getDefault(), "Total: \u20B9%.2f", booking.getTotalAmount()));
    }

    private void setupClickListeners() {
        binding.btnViewMyBookings.setOnClickListener(v -> {
            startActivity(new Intent(this, MyBookingsActivity.class));
            finish();
        });

        binding.btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
