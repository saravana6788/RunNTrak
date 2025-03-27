package com.skcodes.run.domain

import com.skcodes.core.domain.LocationTimeStamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {

    fun getTotalDistanceInMeters(locationsList:List<List<LocationTimeStamp>>):Int{
        return locationsList
            .sumOf { timeStampsPerLine ->
                timeStampsPerLine
                    .zipWithNext { location1, location2 ->
                        location1.locationWithAltitude.location.distanceTo(
                            location2.locationWithAltitude.location
                        )
                    }
                    .sum().roundToInt()

            }
    }

    fun getMaxSpeedInKmh(locationsList:List<List<LocationTimeStamp>>): Double {
        return locationsList.maxOf { locationSet ->
            locationSet.zipWithNext{ location1,location2 ->
                val distanceInMeters = location1.locationWithAltitude.location.distanceTo(
                location2.locationWithAltitude.location)

                val hourTimeDiff = (location2.timeStamp - location1.timeStamp).toDouble(DurationUnit.HOURS)

                if(hourTimeDiff == 0.0){
                    0.0
                }else{
                    (distanceInMeters/1000.0)/ hourTimeDiff
                }

            }.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationInMeters(locationsList:List<List<LocationTimeStamp>>): Int {

        return locationsList.sumOf { locationSet ->
            locationSet.zipWithNext{ location1,location2 ->
                (location2.locationWithAltitude.altitude - location1.locationWithAltitude.altitude).coerceAtLeast(0.0)
            }.sum().roundToInt()
        }

    }
}