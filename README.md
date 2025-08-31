PlaceFinder: A Location-Aware Recommendation App

PlaceFinder is a full-stack project that provides users with a curated list of nearby points of interest, such as tourist spots and hotels, based on their current GPS location. It features a lightweight Python backend that communicates with the Google Places API and a native Android client that displays the results in a clean, image-rich user interface.

Features
Real-Time Location Tracking: The Android app gets the user's current location using the device's GPS.

Dynamic Recommendations: Fetches a list of nearby places from the backend based on the user's location.

Photo-Rich UI: Displays each location with a name, address, rating, and a photo for a visually appealing experience.

RESTful Backend: A Python Flask server acts as a secure middleware between the client and the Google Places API.

Optimized Performance: Implements server-side image resizing and efficient client-side image caching with Glide for a fast and smooth user experience.

Tech Stack
This project is built with a modern, robust tech stack for both the backend and frontend.

Backend
Language: Python 3

Framework: Flask

Networking: requests library for making HTTP requests to external APIs.

API: Google Places API for sourcing location data.

Server: Flask's built-in Werkzeug WSGI server (for development).

Frontend (Android)
Language: Kotlin

Platform: Android SDK

Architecture: ViewModel for UI state management.

Networking:

Retrofit: For type-safe HTTP requests to the backend API.

Moshi: For efficient and modern JSON parsing.

UI & Graphics:

RecyclerView & CardView: For displaying a dynamic list of places.

Glide: For high-performance image loading and caching.

Location Services: Google Play Services FusedLocationProviderClient for getting the device's location.
