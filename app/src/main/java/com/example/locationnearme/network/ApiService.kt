package com.example.locationnearme.network

import com.example.locationnearme.data.LocationRequest
import com.example.locationnearme.data.Place
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("get_nearby_places")
    suspend fun getNearbyPlaces(@Body location: LocationRequest): Response<List<Place>>
}