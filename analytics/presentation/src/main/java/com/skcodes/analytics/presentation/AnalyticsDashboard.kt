package com.skcodes.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skcodes.analytics.presentation.components.AnalyticsCard
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.RunNTrakScaffold
import com.skcodes.presentation.designsystem.components.RunNTrakToolBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClick:() -> Unit,
    viewModel: AnalyticsDashboardViewModel = koinViewModel()
){
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
              AnalyticsAction.OnBackClick ->  onBackClick()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDashboardScreen(
    state:AnalyticsDashboardState?,
    onAction:(AnalyticsAction) -> Unit
){
    RunNTrakScaffold(
        modifier = Modifier.fillMaxSize(),
        withGradient = true,
        topAppBar = {
            RunNTrakToolBar(
                title = stringResource(R.string.analytics),
                showBackButton = true,
                onBackClick = {
                    onAction(AnalyticsAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {}
    ) {

        if(state == null){
            Box(modifier = Modifier.fillMaxSize()
                .padding(it),
                contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
        }else{
            Column(modifier = Modifier.fillMaxSize()
                .padding(it),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                    ){

                    AnalyticsCard(
                        title  = stringResource(R.string.total_distance_run),
                        value = state.totalDistanceRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title  = stringResource(R.string.total_time_run),
                        value = state.totalTimeRun,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                ){

                    AnalyticsCard(
                        title  = stringResource(R.string.fastest_ever_run),
                        value = state.fastestEverRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title  = stringResource(R.string.avg_distance_per_run),
                        value = state.avgDistance,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                ){

                    AnalyticsCard(
                        title  = stringResource(R.string.avg_pace_per_run),
                        value = state.avgPace,
                        modifier = Modifier.weight(1f)
                    )
                   
                }

            }
        }

    }
    

}




@Preview
@Composable
fun AnalyticsDashboardPreview(
){
    RunNTrakTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "23.0",
                totalTimeRun = "10:10:00",
                fastestEverRun = "3.45",
                avgDistance = "20",
                avgPace = "4.5",
            ),
            onAction = {}
        )
    }
}