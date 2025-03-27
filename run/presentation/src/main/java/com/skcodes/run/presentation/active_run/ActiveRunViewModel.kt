package com.skcodes.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skcodes.core.domain.Location
import com.skcodes.core.domain.run.Run
import com.skcodes.core.domain.run.RunRepository
import com.skcodes.core.domain.util.Result
import com.skcodes.presentation.ui.asUIText
import com.skcodes.run.domain.LocationDataCalculator
import com.skcodes.run.domain.RunningTracker
import com.skcodes.run.presentation.active_run.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val runRepository: RunRepository
):ViewModel() {

    var state by  mutableStateOf(ActiveRunState(
        shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
        hasStartedRunning = ActiveRunService.isServiceActive
    ))
        private set

   private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)

    private val shouldTrack = snapshotFlow {
        state.shouldTrack
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    private val isTracking = combine( shouldTrack, _hasLocationPermission){ shouldTrack,hasLocationPermission ->
        shouldTrack && hasLocationPermission
    }.stateIn(viewModelScope,
        SharingStarted.Lazily,
        false
    )



    init{
        _hasLocationPermission.onEach {
            if(it){
                runningTracker.startObservingLocation()
            }else{
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)

        isTracking
            .onEach {
                runningTracker.setIsTracking(isTracking = it)
            }.launchIn(viewModelScope)

        runningTracker.currentLocation
            .onEach {
                state = state.copy(
                    currentLocation = it.location
                )
            }.launchIn(viewModelScope)

        runningTracker.runData
            .onEach {
                state = state.copy(
                    runData = it
                )
            }.launchIn(viewModelScope)

        runningTracker.elapsedTime
            .onEach {
                state = state.copy(
                    elapsedTime = it
                )
            }.launchIn(viewModelScope)
    }

    fun onAction(action:ActiveRunAction){
        when(action){
            ActiveRunAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }
            ActiveRunAction.OnFinishClick -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }
            ActiveRunAction.OnResumeClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }
            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }
            is ActiveRunAction.SubmitNotificationPermissionInfo ->{
                state =  state.copy(
                    showNotificationRationale = action.shouldShowNotificationPermissionRationale
                )
            }
            is ActiveRunAction.SubmitRequestLocationPermissionInfo -> {
                _hasLocationPermission.value = action.acceptedLocationPermissions
                state = state.copy(
                    showLocationRationale = action.shouldShowLocationPermissionRationale
                )
            }

            ActiveRunAction.OnDismissDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }

            is ActiveRunAction.OnRunProcessed -> {
                finishRun(action.mapPicInBytes)
            }
        }




    }

    private fun finishRun(mapPicInBytes: ByteArray) {
        val locations = state.runData.locations

        if(locations.isEmpty() || locations.size <=1){
            state = state.copy(isSavingRun = false)
            return
        }

        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                distanceInMeters = state.runData.distanceInMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                dataTimeInUTC = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                maxSpeedInKmh = LocationDataCalculator.getMaxSpeedInKmh(locations),
                totalElevationInMeters = LocationDataCalculator.getTotalElevationInMeters(locations),
                mapPictureUrl = null
            )

            runningTracker.finishRun()
            // Save Run in Repository
            when(val result =  runRepository.upsertRun(run, mapPicInBytes)){
                is Result.Error -> {
                    eventChannel.send(ActiveRunEvent.Error(result.error.asUIText()))
                }
                is Result.Success -> {
                   eventChannel.send(ActiveRunEvent.RunSaved)
                }
            }

            state = state.copy(isSavingRun = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if(!ActiveRunService.isServiceActive){
            runningTracker.stopObservingLocation()
        }
    }

}
