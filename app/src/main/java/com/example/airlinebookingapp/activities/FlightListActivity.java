package com.example.airlinebookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.adapters.FlightAdapter;
import com.example.airlinebookingapp.models.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightListActivity extends AppCompatActivity implements FlightAdapter.OnFlightClickListener {
    private RecyclerView recyclerViewFlights;
    private Button btnBackToSearch;
    private FlightAdapter flightAdapter;

    private String from;
    private String to;
    private String date;
    private int passengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flight_list);

        setupEdgeToEdge();
        getIntentExtras();
        initializeViews();
        setupRecyclerView();
        loadFlights();
        setupClickListeners();
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getIntentExtras() {
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
        passengers = getIntent().getIntExtra("passengers", 1);
    }

    private void initializeViews() {
        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        btnBackToSearch = findViewById(R.id.btnBackToSearch);
    }

    private void setupRecyclerView() {
        flightAdapter = new FlightAdapter(new ArrayList<>(), this);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlights.setAdapter(flightAdapter);
    }

    private void setupClickListeners() {
        btnBackToSearch.setOnClickListener(v -> {
            // Go back to the previous activity (assuming it's the search screen)
            finish(); // This will close the current activity and return to the previous one in the stack
        });
    }

    private void loadFlights() {
        // TODO: Replace with actual API call in production
        new Thread(() -> {
            // Simulate network delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create sample flights
            List<Flight> flights = createSampleFlights();

            runOnUiThread(() -> {
                flightAdapter.updateFlights(flights);
            });
        }).start();
    }

    private List<Flight> createSampleFlights() {
        List<Flight> flights = new ArrayList<>();
        // Add sample flights (assuming Flight constructor: flightNumber, departure, destination, departureTime, arrivalTime, price, seats)
        flights.add(new Flight("AI101", from, to, "06:00", "08:30", 199.99, 120));
        flights.add(new Flight("AI102", from, to, "09:30", "12:00", 299.99, 80));
        flights.add(new Flight("AI103", from, to, "14:00", "16:30", 249.99, 100));
        return flights;
    }

    @Override
    public void onBookClick(Flight flight) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("flightNumber", flight.getFlightNumber());
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("date", date);
        intent.putExtra("passengers", passengers);
        startActivity(intent);
    }
}
