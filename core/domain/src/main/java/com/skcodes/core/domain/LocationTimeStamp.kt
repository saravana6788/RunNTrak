package com.skcodes.core.domain

import kotlin.time.Duration

data class LocationTimeStamp(
    val locationWithAltitude: LocationWithAltitude,
    val timeStamp: Duration
)

