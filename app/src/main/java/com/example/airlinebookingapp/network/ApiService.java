package com.example.airlinebookingapp.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.airlinebookingapp.models.User;
import com.example.airlinebookingapp.models.Flight;
import com.example.airlinebookingapp.models.Booking;

import java.util.List;

public interface ApiService {
    // TODO: Replace with your actual API endpoints
    String BASE_URL = "https://api.yourbackend.com/";

    // Auth endpoints
    @POST("auth/login")
    Call<User> login(@Body User user);

    @POST("auth/register")
    Call<User> register(@Body User user);

    // Flight endpoints
    @GET("flights/search")
    Call<List<Flight>> searchFlights(@Query("from") String from,
                                   @Query("to") String to,
                                   @Query("date") String date);

    @GET("flights/{flightNumber}")
    Call<Flight> getFlightDetails(@Path("flightNumber") String flightNumber);

    // Booking endpoints
    @POST("bookings")
    Call<Booking> createBooking(@Body Booking booking);

    @GET("bookings/user/{userId}")
    Call<List<Booking>> getUserBookings(@Path("userId") int userId);

    // Payment endpoints
    // TODO: Implement Stripe/Razorpay integration
    @POST("payments/create")
    Call<String> createPaymentIntent(@Query("amount") double amount);
}
