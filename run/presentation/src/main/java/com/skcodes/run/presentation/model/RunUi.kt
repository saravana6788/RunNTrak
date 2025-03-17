package com.skcodes.run.presentation.model

import kotlin.time.Duration

data class RunUi(
    val id:String,
    val duration: String,
    val distance:String,
    val dateTime:String,
    val maxSpeed:String,
    val pace:String,
    val avgSpeed:String,
    val mapPictureUrl:String?,
    val totalElevationInMeters:String
)
