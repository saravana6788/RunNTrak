@file:OptIn(ExperimentalCoroutinesApi::class)

package com.skcodes.run.domain

import com.skcodes.core.domain.LocationTimeStamp
import com.skcodes.core.domain.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope:CoroutineScope
) {

    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val isObservingLocation = MutableStateFlow(false)


    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if(isObservingLocation){
                locationObserver.observeLocation(1000L)
            }else flowOf()

        }.shareIn(
            scope = applicationScope,
            SharingStarted.Lazily,

        )

    init{

        // calculate the elapsed time
        isTracking
            .onEach { isTracking ->
                if(!isTracking){
                    val newList = buildList{
                        addAll(runData.value.locations)
                        add(emptyList<LocationTimeStamp>())
                    }.toList()

                    _runData.update { it.copy(
                        locations = newList
                    )
                    }
            }

            }
            .flatMapLatest {isTracking ->
                if(isTracking){
                    Timer.timeAndEmit()
                }else flowOf()

            }.onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        // construct LocationWithTimeStamp from location and elapsed time
        currentLocation
            .filterNotNull()
            .combineTransform(isTracking){ location,isTracking ->
                if(isTracking){
                    emit(location)
                }

            }
            .zip(elapsedTime){ location,elapsedTime ->
                LocationTimeStamp(
                    locationWithAltitude = location,
                    timeStamp = elapsedTime

                )
            }
                //construct Rundata data using locationTimestamp
            .onEach { location ->
                val currentLocationsList = runData.value.locations
                val lastLocationsList = if(currentLocationsList.isNotEmpty()){
                    currentLocationsList.last() + location
                } else {
                    listOf(location)
                }

                val newLocationsList = currentLocationsList.replaceLast(lastLocationsList)

                val distanceInMeters = LocationDataCalculator.getTotalDistanceInMeters(newLocationsList)
                val distanceInKm = distanceInMeters / 1000.0
                val currentDuration = location.timeStamp
                val avgSecondsPerKm = if(distanceInKm == 0.0) 0
                            else (currentDuration.inWholeSeconds/distanceInKm).roundToInt()

                _runData.update{
                 RunData(
                     distanceInMeters = distanceInMeters,
                     pace = avgSecondsPerKm.seconds,
                     locations = newLocationsList
                 )
                }

            }.launchIn(applicationScope)
    }


    fun setIsTracking(isTracking:Boolean){
        this._isTracking.value = isTracking
    }

    fun startObservingLocation(){
        isObservingLocation.value = true

    }

    fun stopObservingLocation(){
        isObservingLocation.value = false
    }


    private fun <T> List<List<T>>.replaceLast(replacement:List<T>): List<List<T>>{
        return if(this.isEmpty()){
            listOf(replacement)
        }else{
            this.dropLast(1) + listOf(replacement)

        }

    }

}