package com.skcodes.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skcodes.analytics.domain.AnalyticsRepository
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    val analyticsRepository: AnalyticsRepository
):ViewModel() {

    var state by mutableStateOf<AnalyticsDashboardState?>(null)
    private set


    init{
        viewModelScope.launch {
            state = analyticsRepository.getAnalyticsData().toAnalyticsDashboardState()
        }

    }

}