package com.example.locationnearme.data

import com.squareup.moshi.Json

data class LocationRequest(
    val latitude: Double,
    val longitude: Double,
    @Json(name = "place_type") val placeType: String = "tourist_attraction"
)
