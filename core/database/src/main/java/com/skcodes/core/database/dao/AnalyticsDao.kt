package com.skcodes.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnalyticsDao {

    @Query("SELECT SUM(distanceInMeters) from RunEntity")
    suspend fun getTotalDistance():Int

    @Query("SELECT SUM(durationInMillis) from RunEntity")
    suspend fun getTotalTime():Long

    @Query("SELECT MAX(maxSpeedInKmh) from RunEntity")
    suspend fun getMaxRunSpeed():Double

    @Query("SELECT AVG(distanceInMeters) from RunEntity")
    suspend fun getAvgDistancePerRun():Double

    @Query("SELECT AVG((durationInMillis/60000.0) / (distanceInMeters / 1000.0)  ) from RunEntity")
    suspend fun getAvgPacePerRun():Double





}