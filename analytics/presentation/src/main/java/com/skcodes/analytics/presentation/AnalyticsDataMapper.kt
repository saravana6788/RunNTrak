package com.skcodes.analytics.presentation

import com.skcodes.analytics.domain.AnalyticsValues
import com.skcodes.presentation.ui.formatted
import com.skcodes.presentation.ui.toFormattedKm
import com.skcodes.presentation.ui.toFormattedKmh
import com.skcodes.presentation.ui.toFormattedMeters
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


fun Duration.toFormattedTime():String{
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60
     return "${days}d ${hours}h ${minutes}m"
}
fun AnalyticsValues.toAnalyticsDashboardState():AnalyticsDashboardState{
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistance/1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgDistance = (avgDistanceRun/1000.0).toFormattedKmh(),
        avgPace = avgPacePerRun.seconds.formatted()
    )
}