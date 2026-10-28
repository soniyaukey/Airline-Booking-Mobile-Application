package com.example.airlinebookingapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookings")
public class Booking {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bookingId")
    private String bookingId;

    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "flightId")
    private String flightId;

    @ColumnInfo(name = "bookingDate")
    private String bookingDate;

    @ColumnInfo(name = "passengerCount")
    private int passengerCount;

    @ColumnInfo(name = "totalAmount")
    private double totalAmount;

    @ColumnInfo(name = "paymentStatus")
    private String paymentStatus; // PENDING, COMPLETED, FAILED

    @ColumnInfo(name = "bookingStatus")
    private String bookingStatus; // CONFIRMED, CANCELLED

    @ColumnInfo(name = "paymentMethod")
    private String paymentMethod;

    @ColumnInfo(name = "passengerNames")
    private String passengerNames; // Comma-separated list of names

    public Booking() {
        // Required for Firebase
    }

    public Booking(String userId, String flightId, String bookingDate,
                  int passengerCount, double totalAmount) {
        this.userId = userId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.passengerCount = passengerCount;
        this.totalAmount = totalAmount;
        this.paymentStatus = "PENDING";
        this.bookingStatus = "CONFIRMED";
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPassengerNames() { return passengerNames; }
    public void setPassengerNames(String passengerNames) { this.passengerNames = passengerNames; }
}
