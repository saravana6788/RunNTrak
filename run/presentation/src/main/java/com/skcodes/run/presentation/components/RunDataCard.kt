package com.skcodes.run.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.ui.formatted
import com.skcodes.presentation.ui.toFormattedKm
import com.skcodes.presentation.ui.toFormattedPace
import com.skcodes.run.domain.RunData
import com.skcodes.run.presentation.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun RunDataCard(
    modifier: Modifier = Modifier,
    elapsedTime: Duration,
    runData: RunData
    ){
Column(modifier = modifier
    .clip(RoundedCornerShape(15.dp))
    .background(color = MaterialTheme.colorScheme.surface)
    .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
            RunDataItem(title = stringResource(id = R.string.duration),
                value = elapsedTime.formatted(),
                valueFontSize = 32.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){

                RunDataItem(title = stringResource(id = R.string.distance),
                    value = (runData.distanceInMeters/1000.0).toFormattedKm(),
                    modifier = Modifier.defaultMinSize(75.dp)
                )

                RunDataItem(title = stringResource(id = R.string.pace),
                    value = elapsedTime.toFormattedPace(
                        distanceinKm = runData.distanceInMeters/1000.0
                    ),
                    modifier = Modifier.defaultMinSize(75.dp)
                )

            }

}


}

@Composable
private fun RunDataItem(
    title:String,
    value:String,
    modifier:Modifier = Modifier,
    valueFontSize:TextUnit = 16.sp
){

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = valueFontSize
        )
    }

}


@Preview
@Composable
fun RunDataItemPreview(){
    RunNTrakTheme {
        RunDataItem(
            title = "Duration",
            value = "10:00:00",
            valueFontSize = 18.sp

        )
    }

}


@Preview
@Composable
fun RunDataCardPreview(){
    RunNTrakTheme {
        RunDataCard(
            elapsedTime = 10.minutes,
            runData = RunData(
                distanceInMeters = 3546,
                pace = 3.minutes

            )

        )
    }
}