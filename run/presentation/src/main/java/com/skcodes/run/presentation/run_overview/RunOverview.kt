package com.skcodes.run.presentation.run_overview

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skcodes.presentation.designsystem.AnalyticsIcon
import com.skcodes.presentation.designsystem.LogoIcon
import com.skcodes.presentation.designsystem.LogoutIcon
import com.skcodes.presentation.designsystem.RunIcon
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.RunNTrakFloatingActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakScaffold
import com.skcodes.presentation.designsystem.components.RunNTrakToolBar
import com.skcodes.presentation.designsystem.components.util.DropDownItem
import com.skcodes.run.presentation.R
import com.skcodes.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel


@Composable
fun RunOverviewScreenRoot(
    onStartClick:() ->Unit,
    onLogOutClick:() -> Unit,
    onAnalyticsClick:() -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel()
){

    RunOverviewScreen(
        state = viewModel.state,
        onAction = { action ->

            when(action){
                RunOverviewAction.OnStartRunClick -> onStartClick()
                RunOverviewAction.OnLogOutClick -> onLogOutClick()
                RunOverviewAction.OnAnalyticsClick -> onAnalyticsClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RunOverviewScreen(
    state:RunOverviewState,
    onAction:(RunOverviewAction)->Unit
){
    val topAppBarState  = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    RunNTrakScaffold(
        withGradient = true,
        floatingActionButton = {
            RunNTrakFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.OnStartRunClick) })
        },
        topAppBar = {
            RunNTrakToolBar(modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.runntrak),
                showBackButton = false,
                menuItems = listOf(
                    DropDownItem(icon = AnalyticsIcon, title = stringResource(id = R.string.analytics)),
                    DropDownItem(icon = LogoutIcon, title = stringResource(id = R.string.logout))
                ),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(imageVector = LogoIcon, contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp))
                },
                onMenuItemClick = { index ->
                    when(index){
                        0 -> { onAction(RunOverviewAction.OnAnalyticsClick)}
                        1 ->{ onAction(RunOverviewAction.OnLogOutClick)}
                    }
                })
        }) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(16.dp),
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.runs,
                key = {run -> run.id}
            ){runUi ->
                RunListItem(
                    run = runUi,
                    onDeleteClick = { onAction(RunOverviewAction.DeleteRun(runUi)) },
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }

    }

}

@Preview
@Composable
fun RunOverviewScreenPreview(){
RunNTrakTheme {
    RunOverviewScreen(
        onAction = {},
        state = RunOverviewState()
    )
}
}