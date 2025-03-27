package com.skcodes.analytics.domain

interface AnalyticsRepository {

    suspend fun getAnalyticsData():AnalyticsValues
}