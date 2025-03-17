package com.skcodes.run.domain.run

import com.skcodes.core.domain.Location

import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id:String? = null,
    val duration: Duration,
    val distanceInMeters:Int,
    val location: Location,
    val dataTimeInUTC:ZonedDateTime,
    val maxSpeedInKmh:Double,
    val totalElevationInMeters:Int,
    val mapPictureUrl:String?
){
    val avgSpeedInKmh:Double
        get() = (distanceInMeters/1000.0)/ duration.toDouble(DurationUnit.HOURS)
}