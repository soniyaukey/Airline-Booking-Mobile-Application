# Airline Booking App

A simple Android application for booking airline tickets. This app demonstrates the use of Room database, Retrofit for network calls, and basic Android UI components.

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Update the API base URL in `ApiService.java` to point to your backend server
4. Build and run the project

## Features

- User authentication (login/register)
- Flight search
- Flight booking
- Booking history
- Admin dashboard
- Local storage using Room database
- RESTful API integration (stub)

## Backend Integration

To integrate with your backend:

1. Update the `BASE_URL` in `ApiService.java`
2. Implement the API endpoints in your backend matching the interface
3. Update the models as needed to match your data structure

## Payment Integration

To add payment processing:

1. Add your Stripe/Razorpay API keys in the gradle properties
2. Uncomment and implement the payment methods in the checkout flow
3. Update the backend endpoints for payment processing

## Database

The app uses Room database with the following entities:
- User
- Flight
- Booking

## Testing

The app includes basic unit tests for the database and network layers.

## Security Notes

1. Passwords are hashed using SHA-256 (demo only - use stronger methods in production)
2. API keys should be stored securely (e.g., in gradle.properties)
3. Implement proper SSL certificate pinning for production use

## Development Notes

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Language: Java
- Database: Room
- Network: Retrofit
