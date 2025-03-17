package com.skcodes.run.presentation.run_overview.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.skcodes.presentation.designsystem.CalendarIcon
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.RunOutlinedIcon
import com.skcodes.presentation.designsystem.components.util.DropDownItem
import com.skcodes.run.presentation.R
import com.skcodes.run.presentation.model.RunGrid
import com.skcodes.run.presentation.model.RunUi
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunListItem(
    run:RunUi,
    modifier:Modifier,
    onDeleteClick:()->Unit

    ){

    var showDropDown by remember {
        mutableStateOf(false)
    }
    Box {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = { showDropDown = true }
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RunMapImage(imageUrl = run.mapPictureUrl)
            RunningTimeSection(
                duration = run.duration,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            RunDateSection(
                dateTime = run.dateTime,
                modifier = Modifier.fillMaxWidth()
            )

            RunDetails(
                modifier = Modifier.fillMaxWidth(),
                run = run
            )

        }

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.delete))
                },
                onClick = {
                    showDropDown = false
                    onDeleteClick()
                }
            )
        }
    }
}

@Composable
fun RunMapImage(
    modifier:Modifier = Modifier,
    imageUrl:String?
){
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.run_map),
        modifier = modifier.fillMaxWidth()
            .aspectRatio(16/9f)
            .clip(RoundedCornerShape(15.dp)),
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        },
        error = {
            Box(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.cound_not_load_image),
                    color = MaterialTheme.colorScheme.error)
            }
        }
    )
}


@Composable
fun RunningTimeSection(
    duration:String,
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically

    ){

        Box(modifier = Modifier.size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = RunOutlinedIcon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(R.string.total_running_time),
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            Text(text = duration,
                color = MaterialTheme.colorScheme.onSurface)
        }

    }
}

@Composable
fun RunDateSection(
    dateTime: String,
    modifier: Modifier
){
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            imageVector = CalendarIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.width(16.dp))

        Text(text = dateTime,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

    }
}

@Composable
fun RunDataGridCell(
    modifier:Modifier = Modifier,
    runGrid: RunGrid
){
    Column(modifier = modifier) {
        Text(text = runGrid.title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp)
        Spacer(Modifier.height(4.dp))
        Text(text = runGrid.value,
            color = MaterialTheme.colorScheme.onSurface,
            )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RunDetails(
    run: RunUi,
    modifier:Modifier
){
    val runDatailsList = listOf(
        RunGrid( title = stringResource( R.string.distance),
            value = run.distance),
        RunGrid( title = stringResource( R.string.pace),
            value = run.pace),
        RunGrid( title = stringResource( R.string.Speed),
            value = run.avgSpeed),
        RunGrid( title = stringResource( R.string.max_speed),
            value = run.maxSpeed),
        RunGrid( title = stringResource( R.string.total_elevation),
            value = run.totalElevationInMeters),
    )

    var maxWidth by remember {
        mutableIntStateOf(0)
    }

    val maxWidthInDp = with(LocalDensity.current){
        maxWidth.toDp()
    }
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        runDatailsList.forEach { runGrid ->
            RunDataGridCell(runGrid = runGrid,
                modifier = Modifier.defaultMinSize(minWidth = maxWidthInDp)
                    .onSizeChanged {
                        maxWidth = max(it.width,maxWidth)
                    })
        }
    }
}


@Preview
@Composable
fun RunListItemPreview(){
    RunNTrakTheme {
        RunListItem(
            run = RunUi(
                id = "123",
                duration = "10:30:00",
                distance = "10 Km",
                dateTime = "Mar 16, 2025 - 10:19PM",
                maxSpeed = "45 km/h",
                pace = "15 km/h",
                avgSpeed = "25 km/h",
                mapPictureUrl = "",
                totalElevationInMeters = "5m",
            ),
            modifier = Modifier,
            onDeleteClick = {}
        )
    }

}