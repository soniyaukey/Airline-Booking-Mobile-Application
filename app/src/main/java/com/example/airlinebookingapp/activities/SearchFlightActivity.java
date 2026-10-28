package com.example.airlinebookingapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.airlinebookingapp.R;

import java.util.Calendar;

public class SearchFlightActivity extends AppCompatActivity {
    private EditText etFrom;
    private EditText etTo;
    private EditText etDate;
    private EditText etPassengers;
    private Button btnSearchFlights;
    private TextView tvSwap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_flight);

        setupEdgeToEdge();
        initializeViews();
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
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        etDate = findViewById(R.id.etDate);
        etPassengers = findViewById(R.id.etPassengers);
        btnSearchFlights = findViewById(R.id.btnSearchFlights);
        tvSwap = findViewById(R.id.tvSwap);
    }

    private void setupClickListeners() {
        btnSearchFlights.setOnClickListener(v -> handleSearch());
        tvSwap.setOnClickListener(v -> swapLocations());
        etDate.setOnClickListener(v -> showDatePicker());
    }

    private void handleSearch() {
        String from = etFrom.getText().toString().trim();
        String to = etTo.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String passengers = etPassengers.getText().toString().trim();

        if (!validateInputs(from, to, date, passengers)) {
            return;
        }

        Intent intent = new Intent(this, FlightListActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("date", date);
        intent.putExtra("passengers", Integer.parseInt(passengers));
        startActivity(intent);
    }

    private boolean validateInputs(String from, String to, String date, String passengers) {
        if (TextUtils.isEmpty(from)) {
            etFrom.setError("Please enter departure city");
            return false;
        }
        if (TextUtils.isEmpty(to)) {
            etTo.setError("Please enter destination city");
            return false;
        }
        if (TextUtils.isEmpty(date)) {
            etDate.setError("Please select date");
            return false;
        }
        if (TextUtils.isEmpty(passengers)) {
            etPassengers.setError("Please enter number of passengers");
            return false;
        }
        try {
            int count = Integer.parseInt(passengers);
            if (count <= 0 || count > 6) {
                etPassengers.setError("Enter between 1-6 passengers");
                return false;
            }
        } catch (NumberFormatException e) {
            etPassengers.setError("Invalid number");
            return false;
        }
        if (from.equalsIgnoreCase(to)) {
            Toast.makeText(this, "Departure and destination cannot be same",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void swapLocations() {
        String temp = etFrom.getText().toString();
        etFrom.setText(etTo.getText().toString());
        etTo.setText(temp);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(date);
                },
                year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
