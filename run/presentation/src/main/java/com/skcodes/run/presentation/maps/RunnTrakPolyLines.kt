package com.skcodes.run.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.skcodes.core.domain.LocationTimeStamp

@Composable
fun RunnTrakPolyLines(
    locations:List<List<LocationTimeStamp>>
) {
    val polyLines = remember(locations){
        locations.map {
            it.zipWithNext{locationTimeStamp1,locationTimeStamp2 ->
                PolyLineUi(
                    location1 = locationTimeStamp1.locationWithAltitude.location,
                    location2 = locationTimeStamp2.locationWithAltitude.location,
                    color = PolyLineColorCalculator.locationToColor(
                        location1 = locationTimeStamp1,
                        location2 = locationTimeStamp2
                    )
                )

            }
        }
    }
    polyLines.forEach {
        it.forEach {polyLineUi ->
            Polyline(
                points = listOf(
                    LatLng(polyLineUi.location1.latitude,polyLineUi.location1.longitude),
                    LatLng(polyLineUi.location2.latitude, polyLineUi.location2.longitude)
                ),
                color = polyLineUi.color,
                jointType = JointType.BEVEL
            )
        }

    }
}