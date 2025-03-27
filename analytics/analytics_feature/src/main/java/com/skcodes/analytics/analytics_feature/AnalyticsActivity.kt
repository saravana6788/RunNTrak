package com.skcodes.analytics.analytics_feature

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import com.skcodes.analytics.data.di.analyticsDataModule
import com.skcodes.analytics.presentation.AnalyticsDashboardScreenRoot
import com.skcodes.analytics.presentation.di.analyticsPresentationModule
import com.skcodes.presentation.designsystem.RunNTrakTheme
import org.koin.core.context.loadKoinModules

class AnalyticsActivity:ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        loadKoinModules(
            listOf(
                analyticsPresentationModule,
                analyticsDataModule
            )

        )
        SplitCompat.installActivity(this)

        setContent {
            RunNTrakTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = "analytics_dashboard"
                   ){
                    composable(
                        route = "analytics_dashboard",
                    ){
                        AnalyticsDashboardScreenRoot(
                            onBackClick = { finish() }
                        )
                    }
                }
            }
        }

    }
}