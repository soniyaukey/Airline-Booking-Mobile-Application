package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.db.AppDatabase;
import com.example.airlinebookingapp.models.Booking;
import com.example.airlinebookingapp.models.Flight;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingActivity extends AppCompatActivity {
    private TextView tvFlightName;
    private TextView tvFlightRoute;
    private TextView tvFlightTime;
    private TextView tvFlightPrice;
    private EditText etPassengerName;
    private EditText etPassengerEmail;
    private EditText etPassengerPhone;
    private Spinner spinnerSeatClass;
    private RadioGroup rgPaymentMethod;
    private Button btnConfirmBooking;

    private Flight flight;
    private String date;
    private int passengerCount;
    private static final String PREF_NAME = "AirlineBookingPrefs";
    private static final String KEY_USER_ID = "userId";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        getIntentExtras();
        initializeViews();
        setupSpinner();
        setupClickListeners();
    }

    private void getIntentExtras() {
        String flightNumber = getIntent().getStringExtra("flightNumber");
        date = getIntent().getStringExtra("date");
        passengerCount = getIntent().getIntExtra("passengers", 1);

        executorService.execute(() -> {
            flight = AppDatabase.getInstance(getApplicationContext()).flightDao().getFlightByNumber(flightNumber);
            runOnUiThread(this::loadFlightDetails);
        });
    }

    private void initializeViews() {
        tvFlightName = findViewById(R.id.tvFlightName);
        tvFlightRoute = findViewById(R.id.tvFlightRoute);
        tvFlightTime = findViewById(R.id.tvFlightTime);
        tvFlightPrice = findViewById(R.id.tvFlightPrice);
        etPassengerName = findViewById(R.id.etPassengerName);
        etPassengerEmail = findViewById(R.id.etPassengerEmail);
        etPassengerPhone = findViewById(R.id.etPassengerPhone);
        spinnerSeatClass = findViewById(R.id.spinnerSeatClass);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.seat_class_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeatClass.setAdapter(adapter);
    }

    private void loadFlightDetails() {
        if (flight != null) {
            tvFlightName.setText(String.format("Airline: %s", flight.getAirline()));
            tvFlightRoute.setText(String.format("From: %s To: %s", flight.getDeparture(), flight.getDestination()));
            tvFlightTime.setText(String.format("Departure: %s | Arrival: %s", flight.getDepartureTime(), flight.getArrivalTime()));
            tvFlightPrice.setText(String.format("Price: \u20B9 %.2f", flight.getPrice()));
        } else {
            Toast.makeText(this, "Flight details not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnConfirmBooking.setOnClickListener(v -> handleBooking());
    }

    private void handleBooking() {
        String passengerName = etPassengerName.getText().toString().trim();
        String passengerEmail = etPassengerEmail.getText().toString().trim();
        String passengerPhone = etPassengerPhone.getText().toString().trim();
        int selectedPaymentId = rgPaymentMethod.getCheckedRadioButtonId();

        if (!validateInputs(passengerName, passengerEmail, passengerPhone, selectedPaymentId)) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String userId = prefs.getString(KEY_USER_ID, null);

        if (userId == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        double totalAmount = flight.getPrice() * passengerCount;

        Booking booking = new Booking(userId, flight.getFlightNumber(), currentDate,
                passengerCount, totalAmount);
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setPassengerNames(passengerName);

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.bookingDao().insert(booking);

            runOnUiThread(() -> {
                Intent intent = new Intent(this, BookingConfirmationActivity.class);
                intent.putExtra("bookingId", booking.getBookingId());
                startActivity(intent);
                finish();
            });
        });
    }

    private boolean validateInputs(String name, String email, String phone, int paymentId) {
        if (TextUtils.isEmpty(name)) {
            etPassengerName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            etPassengerEmail.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etPassengerEmail.setError("Please enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            etPassengerPhone.setError("Phone number is required");
            return false;
        }
        if (paymentId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
