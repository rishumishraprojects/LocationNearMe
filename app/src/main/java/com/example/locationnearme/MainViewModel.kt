package com.example.locationnearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationnearme.data.LocationRequest
import com.example.locationnearme.data.Place
import com.example.locationnearme.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun findNearbyPlaces(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LocationRequest(latitude, longitude)
                val response = RetrofitInstance.api.getNearbyPlaces(request)
                if (response.isSuccessful) {
                    _places.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to connect: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}