package com.skcodes.run.presentation.active_run

import com.skcodes.core.domain.Location
import com.skcodes.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState (
    val elapsedTime:Duration = Duration.ZERO,
    val shouldTrack:Boolean = false,
    val hasStartedRunning:Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished:Boolean = false,
    val isSavingRun:Boolean = false,
    val runData: RunData = RunData(),
    val showLocationRationale:Boolean = false,
    val showNotificationRationale:Boolean = false
)