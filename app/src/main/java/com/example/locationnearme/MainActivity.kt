package com.example.locationnearme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.locationnearme.Ui.PlacesAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var findPlacesButton: Button
    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                getCurrentLocationAndFetchPlaces()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                getCurrentLocationAndFetchPlaces()
            }
            else -> {

                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize views
        findPlacesButton = findViewById(R.id.findPlacesButton)
        placesRecyclerView = findViewById(R.id.placesRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        setupObservers()

        findPlacesButton.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun setupRecyclerView() {
        placesAdapter = PlacesAdapter(emptyList())
        placesRecyclerView.adapter = placesAdapter
    }

    private fun setupObservers() {
        viewModel.places.observe(this) { places ->
            placesAdapter.updatePlaces(places)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocationAndFetchPlaces()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Optionally show a dialog explaining why you need the permission
                Toast.makeText(this, "We need location to find places near you.", Toast.LENGTH_LONG).show()
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else -> {
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    private fun getCurrentLocationAndFetchPlaces() {
        // Double-check permission before accessing location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        viewModel.findNearbyPlaces(location.latitude, location.longitude)
                    } else {
                        Toast.makeText(this, "Could not get location. Please try again.", Toast.LENGTH_SHORT).show()
                        Log.e("MainActivity", "Location is null")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Failed to get location", it)
                }
        }
    }
}