package com.skcodes.run.presentation.di

import com.skcodes.run.presentation.active_run.ActiveRunViewModel
import com.skcodes.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import com.skcodes.run.domain.RunningTracker

import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)


}