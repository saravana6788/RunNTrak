package com.skcodes.run.domain

import com.skcodes.core.domain.LocationTimeStamp
import kotlin.time.Duration

data class RunData (
    val distanceInMeters:Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations:List<List<LocationTimeStamp>> = emptyList()
)