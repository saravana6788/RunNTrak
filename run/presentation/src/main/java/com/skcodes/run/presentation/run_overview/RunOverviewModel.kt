package com.skcodes.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skcodes.core.domain.SessionStorage
import com.skcodes.core.domain.run.RunRepository
import com.skcodes.core.domain.run.SyncRunScheduler
import com.skcodes.run.presentation.mapper.toRunUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class RunOverviewViewModel(
    private val runRepository: RunRepository,
    private val syncRunScheduler: SyncRunScheduler,
    private val applicationScope:CoroutineScope,
    private val sessionStorage: SessionStorage
):ViewModel() {

    var state by mutableStateOf(RunOverviewState())
        private set

    init{
        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                SyncRunScheduler.SyncType.FetchRun(interval = 30.minutes)
            )
        }

        runRepository.getRuns().onEach { runs ->
            val runUIs = runs.map { it.toRunUi() }
            state = state.copy(runs = runUIs)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.syncPendingRuns() // sync to network before fetching from network
            runRepository.fetchRuns()
        }
    }

    fun onAction(action:RunOverviewAction){
        when(action){
            RunOverviewAction.OnAnalyticsClick -> Unit
            RunOverviewAction.OnLogOutClick -> logout()
            RunOverviewAction.OnStartRunClick -> Unit
            is RunOverviewAction.DeleteRun ->{
                viewModelScope.launch {
                    runRepository.deleteRun(action.runUi.id)
                }

            }
        }
    }


    private fun logout(){
        applicationScope.launch {
            syncRunScheduler.cancelAllSyncs()
            runRepository.deleteAllRuns()
            runRepository.logout()
            sessionStorage.set(null)
        }

    }




}