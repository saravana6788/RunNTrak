package com.skcodes.run.presentation.maps

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.skcodes.core.domain.LocationTimeStamp
import kotlin.math.abs

object PolyLineColorCalculator {

    fun locationToColor(location1:LocationTimeStamp,location2:LocationTimeStamp):Color{
        val distanceInMeters = location2.locationWithAltitude.location.distanceTo(
            location1.locationWithAltitude.location)
        val timeTaken = abs((location2.timeStamp - location1.timeStamp).inWholeSeconds)
        val speedInKmh = (distanceInMeters/timeTaken)*3.6

       return colorInterpolator(
            speedInKmh,
            maxSpeed = 20.0,
            minSpeed = 5.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )

    }

    private fun colorInterpolator(
        speedKmh:Double,
        maxSpeed:Double,
        minSpeed:Double,
        colorStart: Color,
        colorEnd:Color,
        colorMid:Color
        ):Color{

        val ratio = (speedKmh-minSpeed)/(maxSpeed-minSpeed).coerceIn(0.0..1.0)
        val colorInt = if(ratio<=0.5){
            val colorStartToMid = ratio/0.5
            ColorUtils.blendARGB(colorStart.toArgb(),colorMid.toArgb(),colorStartToMid.toFloat())
        }else{
            val colorMidToEnd = (ratio - 0.5)/0.5
            ColorUtils.blendARGB(colorMid.toArgb(),colorEnd.toArgb(),colorMidToEnd.toFloat())
        }
        return Color(colorInt)


    }
}