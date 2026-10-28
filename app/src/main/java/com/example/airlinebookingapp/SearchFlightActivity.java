package com.example.airlinebookingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        initializeViews();
        setupClickListeners();
        setupDatePicker();
        setupEdgeToEdge();
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
        btnSearchFlights.setOnClickListener(v -> handleSearchFlights());
        tvSwap.setOnClickListener(v -> swapCities());
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
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
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void swapCities() {
        String temp = etFrom.getText().toString();
        etFrom.setText(etTo.getText().toString());
        etTo.setText(temp);
    }

    private void handleSearchFlights() {
        String from = etFrom.getText().toString().trim();
        String to = etTo.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String passengers = etPassengers.getText().toString().trim();

        if (from.isEmpty() || to.isEmpty() || date.isEmpty() || passengers.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, FlightResultsActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("date", date);
        intent.putExtra("passengers", passengers);
        startActivity(intent);
    }
}
