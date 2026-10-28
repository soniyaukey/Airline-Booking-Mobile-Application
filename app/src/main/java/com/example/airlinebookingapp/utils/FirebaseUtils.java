package com.example.airlinebookingapp.utils;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.airlinebookingapp.models.User;
import com.example.airlinebookingapp.models.Flight;
import com.example.airlinebookingapp.models.Booking;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtils {
    private static final String TAG = "FirebaseUtils";
    // Firebase instance constants
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Collection names
    private static final String USERS_COLLECTION = "users";
    private static final String FLIGHTS_COLLECTION = "flights";
    private static final String BOOKINGS_COLLECTION = "bookings";

    // Authentication methods
    public static Task<Void> registerUser(String email, String password, String name) {
        return auth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        User user = new User(name, email, "");
                        user.setUid(firebaseUser.getUid());
                        return db.collection(USERS_COLLECTION)
                                .document(firebaseUser.getUid())
                                .set(user);
                    }
                    throw task.getException();
                });
    }

    public static Task<QuerySnapshot> searchFlights(String from, String to, String date) {
        return db.collection(FLIGHTS_COLLECTION)
                .whereEqualTo("departure", from)
                .whereEqualTo("destination", to)
                .whereEqualTo("flightDate", date)
                .get();
    }

    public static Task<Void> createBooking(Booking booking) {
        return db.collection(BOOKINGS_COLLECTION)
                .document()
                .set(booking);
    }

    public static Task<QuerySnapshot> getUserBookings(String userId) {
        return db.collection(BOOKINGS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get();
    }

    public static void addSampleFlights() {
        // Add sample flight data
        Flight[] sampleFlights = {
            new Flight("AI101", "Mumbai", "Delhi", "06:00", "08:00", 199.99, 150),
            new Flight("AI102", "Delhi", "Mumbai", "10:00", "12:00", 249.99, 150),
            new Flight("AI103", "Bangalore", "Mumbai", "14:00", "16:00", 179.99, 150)
        };

        for (Flight flight : sampleFlights) {
            db.collection(FLIGHTS_COLLECTION).add(flight);
        }
    }

    public static boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public static void logout() {
        auth.signOut();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public static void testFirebaseConnection() {
        // Test Firestore
        db.collection(FLIGHTS_COLLECTION)
            .limit(1)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                Log.d(TAG, "Firestore connection successful");
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Firestore connection failed", e);
            });

        // Test Authentication
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "Firebase Auth: User is signed in");
        } else {
            Log.d(TAG, "Firebase Auth: No user signed in");
        }
    }

    // Add test user method
    public static void addTestUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(result -> {
                Log.d(TAG, "Test user created successfully");
                FirebaseUser user = result.getUser();
                if (user != null) {
                    User newUser = new User("Test User", email, "");
                    newUser.setUid(user.getUid());
                    db.collection(USERS_COLLECTION)
                        .document(user.getUid())
                        .set(newUser)
                        .addOnSuccessListener(aVoid ->
                            Log.d(TAG, "User profile created"))
                        .addOnFailureListener(e ->
                            Log.e(TAG, "Error creating user profile", e));
                }
            })
            .addOnFailureListener(e ->
                Log.e(TAG, "Error creating test user", e));
    }

    // Test flight data
    public static void addTestFlights() {
        Flight[] testFlights = {
            new Flight("TEST101", "Mumbai", "Delhi", "10:00", "12:00", 199.99, 100),
            new Flight("TEST102", "Delhi", "Mumbai", "14:00", "16:00", 249.99, 100)
        };

        for (Flight flight : testFlights) {
            db.collection(FLIGHTS_COLLECTION)
                .add(flight)
                .addOnSuccessListener(documentReference ->
                    Log.d(TAG, "Test flight added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                    Log.e(TAG, "Error adding test flight", e));
        }
    }
}
