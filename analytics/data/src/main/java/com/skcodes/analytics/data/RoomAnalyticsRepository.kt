package com.skcodes.analytics.data

import com.skcodes.analytics.domain.AnalyticsRepository
import com.skcodes.analytics.domain.AnalyticsValues
import com.skcodes.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
): AnalyticsRepository{
    override suspend fun getAnalyticsData(): AnalyticsValues {
        return withContext(Dispatchers.IO){
            val totalDistanceRun = async { analyticsDao.getTotalDistance()}
            val totalTimeRun = async { analyticsDao.getTotalTime()}
            val fastestEverRun = async { analyticsDao.getMaxRunSpeed()}
            val avgPacePerRun = async { analyticsDao.getAvgPacePerRun()}
            val avgDistancePerRun = async { analyticsDao.getAvgDistancePerRun()}

            AnalyticsValues(
                totalTimeRun = totalTimeRun.await().milliseconds,
                totalDistance = totalDistanceRun.await(),
                fastestEverRun = fastestEverRun.await(),
                avgDistanceRun = avgDistancePerRun.await(),
                avgPacePerRun = avgPacePerRun.await()
            )

        }
    }
}