package com.skcodes.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    val durationInMillis:Long,
    val distanceInMeters:Int,
    val dateTImeUtc:String,
    val maxSpeedInKmh:Double,
    val avgSpeedInKmh:Double,
    val latitude:Double,
    val longitude:Double,
    val totalElevation:Int,
    val mapPictureUrl:String?,
    @PrimaryKey(autoGenerate = false)
    val id:String = ObjectId().toHexString()
)