package com.skcodes.run.presentation.maps

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.awaitSnapshot
import com.skcodes.core.domain.Location
import com.skcodes.core.domain.LocationTimeStamp
import com.skcodes.presentation.designsystem.RunIcon
import com.skcodes.run.presentation.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class, DelicateCoroutinesApi::class)
@Composable
fun TrackerMap(
    isRunFinished:Boolean,
    currentLocation: Location?,
    locations:List<List<LocationTimeStamp>>,
    modifier: Modifier = Modifier,
    onSnapshot:(Bitmap) -> Unit
    ) {
    val context = LocalContext.current
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(
            context, R.raw.map_style
        )
    }
    val cameraPositionState  = rememberCameraPositionState()
    val markerState = rememberMarkerState()

    val markerStateLat by animateFloatAsState(
        targetValue = currentLocation?.latitude?.toFloat()?:0f,
        animationSpec = tween(durationMillis = 500)
    )

    val markerStateLong by animateFloatAsState(
        targetValue = currentLocation?.longitude?.toFloat()?:0f,
        animationSpec = tween(durationMillis = 500)
    )

    val markerPosition = remember(markerStateLat,markerStateLong) {
        LatLng(markerStateLat.toDouble(),markerStateLong.toDouble())
    }

    LaunchedEffect(markerPosition,isRunFinished) {
        if(!isRunFinished){
            markerState.position = markerPosition
        }
    }

    LaunchedEffect(isRunFinished,currentLocation) {
        if(!isRunFinished && currentLocation!=null){
            val latLang = LatLng(currentLocation.latitude,currentLocation.longitude)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLang,17f)
            )

        }
    }

    var triggerCapture by remember{
        mutableStateOf(false)
    }

    var createSnapshotJob:Job? = remember {
        null
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties =  MapProperties(
            mapStyleOptions = mapStyle
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        modifier = if(isRunFinished){
            modifier.size(300.dp)
                .aspectRatio(16/9f)
                .alpha(0.0f)
                .onSizeChanged {
                    if(it.width>=300){ // condition makes sure that the triggerCapture is not changed when width is 0
                    triggerCapture = true
                        }
                }
        }else modifier
    ){
        RunnTrakPolyLines(
            locations = locations
        )

        MapEffect(locations,triggerCapture,isRunFinished,createSnapshotJob) {map ->
            if(createSnapshotJob==null && triggerCapture && isRunFinished){
                triggerCapture = false

                val boundsBuilder = LatLngBounds.Builder()
                    locations.flatten().forEach { location ->
                        boundsBuilder
                            .include(
                                LatLng(location.locationWithAltitude.location.latitude,
                                    location.locationWithAltitude.location.longitude)
                            )
                    }

                map.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        100
                    )
                )

                map.setOnCameraIdleListener {
                    createSnapshotJob?.cancel()
                    createSnapshotJob = GlobalScope.launch { // GlobalScope as screenshot has to be taken even when lifecycle ends
                        delay(500L)
                        map.awaitSnapshot()?.let(onSnapshot)
                    }
                }
            }

        }
         if(!isRunFinished && currentLocation!=null){
             MarkerComposable(currentLocation,
                 state = markerState) {
                 Box(modifier = Modifier.size(35.dp)
                     .clip(CircleShape)
                     .background(color = MaterialTheme.colorScheme.primary),
                     contentAlignment = Alignment.Center){

                     Icon(imageVector = RunIcon,
                         contentDescription = "",
                         tint = MaterialTheme.colorScheme.onPrimary,
                         modifier = Modifier.size(20.dp))

                 }
             }
         }
    }
}