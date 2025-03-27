package com.skcodes.analytics.presentation

sealed interface AnalyticsAction{
    data object OnBackClick:AnalyticsAction
}