package com.example.airlinebookingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.airlinebookingapp.models.Flight;

import java.util.List;

@Dao
public interface FlightDao {
    @Insert
    void insertAll(Flight... flights);

    @Insert
    void insert(Flight flight);

    @Query("SELECT * FROM flights")
    List<Flight> getAll();

    @Query("SELECT * FROM flights WHERE flightNumber = :flightNumber LIMIT 1")
    Flight getFlightByNumber(String flightNumber);
}
