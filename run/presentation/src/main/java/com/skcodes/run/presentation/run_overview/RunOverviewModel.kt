package com.skcodes.run.presentation.run_overview

import androidx.lifecycle.ViewModel

class RunOverviewViewModel():ViewModel() {

    fun onAction(action:RunOverviewAction){
        when(action){
            RunOverviewAction.OnAnalyticsClick -> TODO()
            RunOverviewAction.OnLogOutClick -> TODO()
            RunOverviewAction.OnStartRunClick -> TODO()
        }
    }


}