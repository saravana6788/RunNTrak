package com.skcodes.run.location

import android.location.Location
import com.skcodes.core.domain.LocationWithAltitude

fun Location.toLocationWithAltitudes():LocationWithAltitude{
    return LocationWithAltitude(
        location = com.skcodes.core.domain.Location(
            latitude = latitude,
            longitude = longitude),
        altitude = altitude,
    )
}
