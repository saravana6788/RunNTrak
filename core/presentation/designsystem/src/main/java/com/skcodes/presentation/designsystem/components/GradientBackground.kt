package com.skcodes.presentation.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skcodes.presentation.designsystem.RunNTrakTheme

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    isToolbarShown:Boolean = true,
    content:@Composable ColumnScope.()->Unit
){
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val isAtLeastAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val screenWidthInPx = with(density){
        configuration.screenWidthDp.dp.roundToPx()
    }
    val smallDimension = minOf(configuration.screenWidthDp.dp,
        configuration.screenHeightDp.dp)

    val smallDimensionInPx = with(density){
        smallDimension.roundToPx()
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)){
        Box(modifier = modifier
            .fillMaxSize()
            .then(
                if (isAtLeastAndroid12) {
                    Modifier.blur(smallDimension / 4f)
                } else {
                    Modifier
                }
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isAtLeastAndroid12) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        },
                        MaterialTheme.colorScheme.background
                    ),
                    center = Offset(
                        x = screenWidthInPx / 2f,
                        y = -100f
                    ),
                    radius = smallDimensionInPx / 2f
                )
            )){

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(if(isToolbarShown){
                    Modifier
                }else{
                    Modifier.systemBarsPadding()
                })


        ) {
            content()
        }
    }



}

@Preview
@Composable
fun gradientBackgroundPreview(){
    RunNTrakTheme {
        GradientBackground(modifier =Modifier.fillMaxSize()) {
        }
    }

}