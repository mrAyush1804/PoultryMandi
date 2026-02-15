package com.example.poultrymandi.app.Core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poultrymandi.app.feature.auth.presentation.login.LoginRoute
import com.example.poultrymandi.app.feature.auth.presentation.singup.SignUpRoute


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
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
                   navController.navigate(Screen.Login)
               },
               onSignUpSuccess = { userId ->
                   navController.navigate(Screen.Dashboard(userId))
               }
            )
        }
    }
}