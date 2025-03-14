package com.skcodes.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartRunClick:RunOverviewAction
    data object OnLogOutClick:RunOverviewAction
    data object OnAnalyticsClick:RunOverviewAction
}