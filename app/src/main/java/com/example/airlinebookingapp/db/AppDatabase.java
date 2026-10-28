package com.example.airlinebookingapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.airlinebookingapp.models.User;
import com.example.airlinebookingapp.models.Flight;
import com.example.airlinebookingapp.models.Booking;
import com.example.airlinebookingapp.utils.PasswordUtils;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Flight.class, Booking.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Use volatile to ensure atomic access to the variable
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract FlightDao flightDao();
    public abstract BookingDao bookingDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "airline_booking_db")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        // Get the instance within the callback to ensure it's not null
                                        AppDatabase database = getInstance(context);
                                        UserDao userDao = database.userDao();
                                        FlightDao flightDao = database.flightDao();
                                        
                                        // Add admin user
                                        User adminUser = new User("Admin", "admin@airline.com",
                                                PasswordUtils.hashPassword("admin123"));
                                        adminUser.setUid("admin-uid"); // Assign a unique ID for the admin user
                                        adminUser.setAdmin(true);
                                        userDao.insert(adminUser);

                                        // Add sample flights
                                        Flight[] sampleFlights = {
                                                new Flight("AI101", "Mumbai", "Delhi",
                                                        "06:00", "08:00", 199.99, 150),
                                                new Flight("AI102", "Delhi", "Mumbai",
                                                        "10:00", "12:00", 249.99, 150),
                                                new Flight("AI103", "Bangalore", "Mumbai",
                                                        "14:00", "16:00", 179.99, 150)
                                        };
                                        for (Flight flight : sampleFlights) {
                                            flightDao.insert(flight);
                                        }
                                    });
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
