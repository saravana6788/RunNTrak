package com.skcodes.core.database.mapper

import com.skcodes.core.database.entity.RunEntity
import com.skcodes.core.domain.Location
import com.skcodes.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationInMillis.milliseconds,
        distanceInMeters = distanceInMeters,
        location = Location(latitude,longitude),
        dataTimeInUTC = Instant.parse(dateTImeUtc).atZone(ZoneId.of("UTC")),
        maxSpeedInKmh = maxSpeedInKmh,
        totalElevationInMeters = totalElevation,
        mapPictureUrl = mapPictureUrl,
    )
}

fun Run.toRunEntity():RunEntity{
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        distanceInMeters = distanceInMeters,
        durationInMillis = duration.inWholeMilliseconds,
        dateTImeUtc = dataTimeInUTC.toInstant().toString(),
        maxSpeedInKmh = maxSpeedInKmh,
        avgSpeedInKmh = avgSpeedInKmh,
        latitude = location.latitude,
        longitude = location.longitude,
        totalElevation = totalElevationInMeters,
        mapPictureUrl = mapPictureUrl,
    )
}