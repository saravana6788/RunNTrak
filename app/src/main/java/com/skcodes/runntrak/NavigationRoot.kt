package com.skcodes.runntrak

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.skcodes.presentation.intro.IntroScreenRoot
import com.skcodes.presentation.login.LoginScreenRoot
import com.skcodes.presentation.register.RegisterScreenRoot
import com.skcodes.run.presentation.active_run.ActiveRunScreenRoot
import com.skcodes.run.presentation.active_run.service.ActiveRunService
import com.skcodes.run.presentation.run_overview.RunOverviewScreenRoot

@Composable
fun NavigationRoot(
    navHostController: NavHostController,
    isLoggedIn:Boolean,
    onAnalyticsClick: () -> Unit
){
    NavHost(navController = navHostController,
        startDestination = if(isLoggedIn) "run" else "auth"){
            authNavGraph(navHostController)
            runNavGraph(navHostController,
                onAnalyticsClick
                )
    }
}


private fun NavGraphBuilder.authNavGraph(navHostController: NavHostController){
    navigation(
        startDestination = "intro",
        route = "auth"
    ){
        composable(route = "intro"){
            IntroScreenRoot(onSignUpClick = {
                navHostController.navigate("register")
            },
                onSignInClick = {
                    navHostController.navigate("login")
                })
        }
        
        composable(route = "register"){
            RegisterScreenRoot(
                onSignInClick = { navHostController.navigate("login") {
                   popUpTo("register"){
                       inclusive = true
                       saveState = true

                   }
                    restoreState = true
                } },
                onRegistrationSuccessful = { navHostController.navigate("login") })
        }
        composable(route = "login") {
            LoginScreenRoot(onLoginSuccess = {
                navHostController.navigate(route = "run"){
                    popUpTo("auth"){
                        inclusive = true
                    }
                }
            }, onSignUpClick = {
                navHostController.navigate(route = "register"){
                    popUpTo("login"){
                        inclusive = true
                        saveState = true
                    }
                    restoreState = true
                }
            })
        }


    }
}


private fun NavGraphBuilder.runNavGraph(navHostController: NavHostController,
                                        onAnalyticsClick:() -> Unit){
    navigation(
        startDestination = "run_overview",
        route = "run"
    ){
        composable(route="run_overview"){
            RunOverviewScreenRoot(
                onStartClick = {
                    navHostController.navigate(route = "active_run") {
                        popUpTo("run_overview") {
                            inclusive = false
                            saveState = true
                        }
                        restoreState = true

                    }
                },
                onLogOutClick = {
                    navHostController.navigate("auth") {
                        popUpTo("run") {
                            inclusive = true
                        }
                    }
                },
                onAnalyticsClick = {
                    onAnalyticsClick()
                }

            )
        }

        composable(
            route= "active_run",
            deepLinks = listOf(navDeepLink {
                uriPattern = "runntrak://active_run"
            })
        ){
            val context = LocalContext.current
            ActiveRunScreenRoot(
                onBack = { navHostController.navigateUp() },
                onFinish = { navHostController.navigateUp() },
                serviceToggle = {
                    if(it){
                        context.startService(
                            ActiveRunService.createStartServiceCommand(
                                context = context,
                                MainActivity::class.java
                            )
                        )

                    }else{
                        context.startService(
                            ActiveRunService.createStopServiceCommand(
                                context = context
                            )
                        )

                    }
                }
            )
        }
    }
}