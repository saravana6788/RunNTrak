package com.skcodes.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = RunNTrakGreen,
    background = RunNTrakBlack,
    surface = RunNTrakDarkGray,
    secondary = RunNTrakWhite,
    tertiary = RunNTrakWhite,
    primaryContainer = RunNTrakGreen30,
    onPrimary = RunNTrakBlack,
    onBackground = RunNTrakWhite,
    onSurface = RunNTrakWhite,
    onSurfaceVariant = RunNTrakGray

)

@Composable
fun RunNTrakTheme(
    content: @Composable () -> Unit,

) {
val colorScheme = DarkColorScheme
    val view = LocalView.current
    if(!view.isInEditMode){
        SideEffect {
            val window = (view.context as Activity).window
            //window.statusBarColor = DarkColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = Typography
    )

}