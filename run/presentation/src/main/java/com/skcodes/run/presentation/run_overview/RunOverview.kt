package com.skcodes.run.presentation.run_overview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel()
){

    RunOverviewScreen(
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunOverviewScreen(
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

    }

}

@Preview
@Composable
fun RunOverviewScreenPreview(){
RunNTrakTheme {
    RunOverviewScreen(
        onAction = {}
    )
}
}