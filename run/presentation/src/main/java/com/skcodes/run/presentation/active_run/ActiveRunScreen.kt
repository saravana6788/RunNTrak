package com.skcodes.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.StartIcon
import com.skcodes.presentation.designsystem.StopIcon
import com.skcodes.presentation.designsystem.components.RunNTrakActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakDialog
import com.skcodes.presentation.designsystem.components.RunNTrakFloatingActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakOutlinedActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakScaffold
import com.skcodes.presentation.designsystem.components.RunNTrakToolBar
import com.skcodes.run.presentation.R
import com.skcodes.run.presentation.active_run.service.ActiveRunService
import com.skcodes.run.presentation.components.RunDataCard
import com.skcodes.run.presentation.maps.TrackerMap
import com.skcodes.run.presentation.util.hasLocationPermissions
import com.skcodes.run.presentation.util.hasNotificationPermissions
import com.skcodes.run.presentation.util.shouldShowLocationPermissionRationale
import com.skcodes.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel


@Composable
fun ActiveRunScreenRoot(
    serviceToggle: (Boolean) -> Unit,
    viewModel:ActiveRunViewModel = koinViewModel()
){

    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        serviceToggle = serviceToggle
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveRunScreen(
    state:ActiveRunState,
    serviceToggle:(Boolean)->Unit,
    onAction: (ActiveRunAction) ->Unit
){

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        val hasCoarseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] ==  true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermissions = if(Build.VERSION.SDK_INT >= 33)
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        else true

        val activity = context as ComponentActivity

        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val shouldShowNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(ActiveRunAction.SubmitRequestLocationPermissionInfo(
         acceptedLocationPermissions = hasCoarseLocationPermission && hasFineLocationPermission,
            shouldShowLocationPermissionRationale = showLocationRationale)
        )

        onAction(ActiveRunAction.SubmitNotificationPermissionInfo(
            acceptedNotificationPermissions = hasNotificationPermissions,
            shouldShowNotificationPermissionRationale = shouldShowNotificationRationale
        ))

    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity

        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(ActiveRunAction.SubmitRequestLocationPermissionInfo(
            acceptedLocationPermissions = context.hasLocationPermissions(),
            shouldShowLocationPermissionRationale = showLocationRationale)
        )

        onAction(ActiveRunAction.SubmitNotificationPermissionInfo(
            acceptedNotificationPermissions = context.hasNotificationPermissions(),
            shouldShowNotificationPermissionRationale = showNotificationRationale
        ))

        if(!showLocationRationale && !showNotificationRationale){
            permissionLauncher.requestRunNTrakPermissions(context)
        }
    }

    LaunchedEffect(state.shouldTrack) {
        if(state.shouldTrack && context.hasLocationPermissions() && !ActiveRunService.isServiceActive){
            serviceToggle(true)
        }
    }

    LaunchedEffect(state.isRunFinished) {
        if(state.isRunFinished) {
            serviceToggle(false)
        }
    }





    RunNTrakScaffold(
        withGradient = false,
        floatingActionButton = {
            RunNTrakFloatingActionButton(icon = if(state.shouldTrack){
                StopIcon
            }else{
                StartIcon
            },
                contentDescription = (if(state.shouldTrack){
                    stringResource(id = R.string.start_run)
                }else{
                    stringResource(id = R.string.stop_run)
                }),
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                size = 20.dp
            )
        },
        topAppBar = {
            RunNTrakToolBar(title = stringResource(id = R.string.active_run),
                showBackButton = true,
                onBackClick = { onAction(ActiveRunAction.OnBackClick) }
            )
        }
    ) {padding ->


        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            ){

            TrackerMap(isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                modifier = Modifier.fillMaxSize())

            RunDataCard(elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier= Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )

        }

    }

    if(!state.shouldTrack && state.hasStartedRunning){
        RunNTrakDialog(
            title = stringResource(R.string.run_is_paused),
            description = stringResource(R.string.resume_or_finish_run),
            primaryButton = {
                RunNTrakActionButton(
                    text = stringResource(R.string.resume),
                    onClick = { onAction(ActiveRunAction.OnResumeClick) },
                    isLoading = false,
                    modifier = Modifier.weight(1f),

                )
            },
            secondaryButton = {
                RunNTrakOutlinedActionButton(
                    text = stringResource(R.string.finish),
                    onClick = {onAction(ActiveRunAction.OnFinishClick)},
                    isLoading = state.isSavingRun,
                    modifier = Modifier.weight(1f)
                )
            },
            onDismiss = {onAction(ActiveRunAction.OnResumeClick)}

        )
    }

    if(state.showLocationRationale || state.showNotificationRationale){
        RunNTrakDialog(title = stringResource(id = R.string.permission_required),
            description = when {
                state.showLocationRationale && state.showNotificationRationale ->{
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale ->{
                    stringResource(id = R.string.location_rationale)
                }

                else ->{
                    stringResource(id = R.string.notification_rationale)
                }
            }, primaryButton = {
                RunNTrakOutlinedActionButton( text = stringResource(id = R.string.okay) ,
                    isLoading = false) {
                    onAction(ActiveRunAction.OnDismissDialog)
                    permissionLauncher.requestRunNTrakPermissions(context)
                }
            })
    }

}


fun ActivityResultLauncher<Array<String>>.requestRunNTrakPermissions(
    context: Context
){
    val hasNotificationPermissions = context.hasNotificationPermissions()
    val hasLocationPermissions = context.hasLocationPermissions()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    
    val notificationPermissions = if(Build.VERSION.SDK_INT >= 33){
       arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    }else arrayOf()

    when{
        !hasLocationPermissions && !hasNotificationPermissions ->{
            launch(locationPermissions + notificationPermissions)
        }

        !hasLocationPermissions ->{
            launch(locationPermissions)
        }

        !hasNotificationPermissions ->{
            launch(notificationPermissions)
        }
    }
}


@Preview
@Composable
fun ActiveRunScreenPreview(){
    RunNTrakTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onAction = {},
            serviceToggle = {}
        )
    }

}