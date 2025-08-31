package com.example.locationnearme.data

import com.squareup.moshi.Json

data class Place(
    val name: String?,
    val address: String?,
    val rating: Double?,
    @Json(name = "user_rating_count") val userRatingCount: Int?,
    @Json(name = "photo_url") val photoUrl: String?
)

