package com.example.poultrymandi.app.Core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poultrymandi.app.feature.CompleteProfile.presentation.CompleteProfileRoute
import com.example.poultrymandi.app.feature.Splash.SplashDestination
import com.example.poultrymandi.app.feature.Splash.SplashScreen
import com.example.poultrymandi.app.feature.Splash.SplashViewModel
import com.example.poultrymandi.app.feature.auth.presentation.login.LoginRoute
import com.example.poultrymandi.app.feature.auth.presentation.singup.SignUpRoute
import com.example.poultrymandi.app.feature.home.presentation.Navigation.HomeBottomNavigationScreenHolder

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash
    ) {

        composable<Screen.Splash> {
            val splashViewModel: SplashViewModel = hiltViewModel()
            val destination by splashViewModel.destination.collectAsState()

            LaunchedEffect(destination) {
                when (destination) {
                    SplashDestination.Loading -> { /* Stay on Splash while checking auth */ }
                    SplashDestination.Login -> {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Splash) { inclusive = true }
                        }
                    }
                    SplashDestination.Home -> {
                        navController.navigate(Screen.HomeBottomNavigationScreenHolder) {
                            popUpTo(Screen.Splash) { inclusive = true }
                        }
                    }
                    SplashDestination.CompleteProfile -> {
                        navController.navigate(Screen.CompleteProfile) {
                            popUpTo(Screen.Splash) { inclusive = true }
                        }
                    }
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
                    // After login success, pop Login so back button doesn't return to it
                    navController.navigate(Screen.HomeBottomNavigationScreenHolder) {
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
                    // Navigate to Complete Profile after successful signup, removing signup from stack
                    navController.navigate(Screen.CompleteProfile) {
                        popUpTo(Screen.SignUp) { inclusive = true }
                    }
                },
                webClientId = "1041518266095-j52d0ldnvvholhq0tm5ce0fb5ds73l3u.apps.googleusercontent.com"
            )
        }

        composable<Screen.CompleteProfile> {
            CompleteProfileRoute(
                onProfileCompleted = {
                    // After profile completion, clear entire backstack before going Home
                    navController.navigate(Screen.HomeBottomNavigationScreenHolder) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.HomeBottomNavigationScreenHolder> {
            HomeBottomNavigationScreenHolder(
                onLogout = {
                    // After logout, pop Home stack and go back to Login
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.HomeBottomNavigationScreenHolder) { inclusive = true }
                    }
                }
            )
        }
    }
}
