package com.skcodes.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SweepGradient

@Composable
fun RunNTrakScaffold(
    modifier: Modifier = Modifier,
    withGradient: Boolean,
    floatingActionButton:@Composable () ->Unit,
    topAppBar:@Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit

    ){

    Scaffold(
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) { padding ->
        if(withGradient){
            GradientBackground {
                content(padding)
            }
        }else{
            content(padding)
        }

    }

}