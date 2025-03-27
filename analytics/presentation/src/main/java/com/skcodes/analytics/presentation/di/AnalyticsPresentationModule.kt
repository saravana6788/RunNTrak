package com.skcodes.analytics.presentation.di

import com.skcodes.analytics.presentation.AnalyticsDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val analyticsPresentationModule = module{
    viewModelOf(::AnalyticsDashboardViewModel)
}