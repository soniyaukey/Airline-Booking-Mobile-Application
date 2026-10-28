package com.example.airlinebookingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.airlinebookingapp.models.Booking;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insert(Booking booking);

    @Query("SELECT * FROM bookings WHERE userId = :userId")
    List<Booking> getBookingsByUserId(String userId);

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId LIMIT 1")
    Booking getBookingById(String bookingId);
}
