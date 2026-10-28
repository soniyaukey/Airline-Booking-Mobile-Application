package com.example.airlinebookingapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flights")
public class Flight {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String flightId;
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private int availableSeats;
    private String flightDate;
    private String airline;

    public Flight() {
        // Required for Firebase
    }

    public Flight(String flightNumber, String departure, String destination,
                 String departureTime, String arrivalTime, double price, int availableSeats) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    // Added missing method
    public void setSeatsAvailable(int seats) {
        this.availableSeats = seats;
    }
}
