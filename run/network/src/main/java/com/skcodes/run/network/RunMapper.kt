package com.skcodes.run.network

import com.skcodes.core.domain.Location
import com.skcodes.core.domain.run.Run
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        distanceInMeters = distanceMeters,
        location = Location(lat,long),
        dataTimeInUTC = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        maxSpeedInKmh = maxSpeedKmh,
        totalElevationInMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}


fun Run.toCreateRunRequest():CreateRunRequest{
    return CreateRunRequest(
        id = id!!,
        durationMillis = duration.inWholeMilliseconds,
        epochMillis = dataTimeInUTC.toEpochSecond() * 1000L,
        distanceMeters = distanceInMeters,
        lat = location.latitude,
        long = location.longitude,
        avgSpeedKmh = avgSpeedInKmh,
        maxSpeedKmh = maxSpeedInKmh,
        totalElevationMeters = totalElevationInMeters
    )
}