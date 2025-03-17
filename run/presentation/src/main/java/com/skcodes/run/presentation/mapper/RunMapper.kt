package com.skcodes.run.presentation.mapper

import com.skcodes.presentation.ui.formatted
import com.skcodes.presentation.ui.toFormattedKm
import com.skcodes.presentation.ui.toFormattedKmh
import com.skcodes.presentation.ui.toFormattedMeters
import com.skcodes.presentation.ui.toFormattedPace
import com.skcodes.run.domain.run.Run
import com.skcodes.run.presentation.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi{
    val dataTimeInLocalTime = dataTimeInUTC.withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma")
        .format(dataTimeInLocalTime)
    val distanceKm =  distanceInMeters/ 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        maxSpeed = maxSpeedInKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        avgSpeed = avgSpeedInKmh.toFormattedKmh(),
        mapPictureUrl = mapPictureUrl,
        totalElevationInMeters = totalElevationInMeters.toFormattedMeters()
    )
}