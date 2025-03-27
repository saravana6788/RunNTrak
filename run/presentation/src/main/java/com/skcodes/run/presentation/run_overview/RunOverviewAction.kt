package com.skcodes.run.presentation.run_overview

import com.skcodes.run.presentation.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartRunClick:RunOverviewAction
    data object OnLogOutClick:RunOverviewAction
    data object OnAnalyticsClick:RunOverviewAction
    class DeleteRun(val runUi: RunUi):RunOverviewAction
}