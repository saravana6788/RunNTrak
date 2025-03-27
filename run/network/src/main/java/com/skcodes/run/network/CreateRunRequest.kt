package com.skcodes.run.network

import kotlinx.serialization.Serializable

@Serializable
data class CreateRunRequest(
    val id: String,
    val durationMillis: Long,
    val epochMillis: Long,
    val distanceMeters: Int,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int
)
