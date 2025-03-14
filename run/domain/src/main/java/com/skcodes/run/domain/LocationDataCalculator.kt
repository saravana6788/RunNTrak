package com.skcodes.run.domain

import com.skcodes.core.domain.LocationTimeStamp
import kotlin.math.roundToInt

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
}