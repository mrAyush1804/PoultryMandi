package com.example.poultrymandi.app.Core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poultrymandi.app.feature.Splash.SplashScreen
import com.example.poultrymandi.app.feature.auth.presentation.login.LoginRoute
import com.example.poultrymandi.app.feature.auth.presentation.singup.SignUpRoute
import com.example.poultrymandi.app.feature.home.presentation.HomeRoute


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash
    ) {

        composable<Screen.Splash>{
            LaunchedEffect(key1 = true)
            {
                kotlinx.coroutines.delay(6000)
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Splash) { inclusive = true }
                }
            }
            SplashScreen()

        }
        composable<Screen.Login> {
            LoginRoute(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp)
                },
                onLoginSuccess = { userId ->
                    navController.navigate(Screen.Dashboard(userId)) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.SignUp> {
            SignUpRoute(
               onNavigateToLogin = {
                   navController.navigate(Screen.Home)
               },
               onSignUpSuccess = { userId ->
                   navController.navigate(Screen.Dashboard(userId))
               }
            )
        }

        composable<Screen.Home>{
            HomeRoute(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login)
                },
                onNavigateToSignUp = {}
            )

        }
    }
}