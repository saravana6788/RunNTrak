package com.skcodes.runntrak

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.skcodes.presentation.intro.IntroScreenRoot
import com.skcodes.presentation.register.RegisterScreenRoot

@Composable
fun NavigationRoot(
    navHostController: NavHostController
){
    NavHost(navController = navHostController,
        startDestination = "auth"){
            authNavGraph(navHostController)
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
        composable("login") {
            Text(text = "Login Page")
        }
    }
}