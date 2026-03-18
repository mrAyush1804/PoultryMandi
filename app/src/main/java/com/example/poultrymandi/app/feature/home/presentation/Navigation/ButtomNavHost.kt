package com.example.poultrymandi.app.feature.home.presentation.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poultrymandi.app.Core.navigation.Screen
import com.example.poultrymandi.app.feature.home.presentation.HomeRoute
import com.example.poultrymandi.app.feature.notification.presentation.NotficationRoute
import com.example.poultrymandi.app.feature.profile.presentation.ProfileScreenRoute

// BottomNavGraph.kt
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onBottomBarVisibilityChanged: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<Screen.Home> {

            HomeRoute(

                currentRoute = Screen.Home,
                onNavigateToNotifications = {
                    navController.navigate(Screen.Notifications)
                },

                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                },

                onBottomBarVisibilityChanged = onBottomBarVisibilityChanged
            )





        }
        composable<Screen.Notifications> {
            NotficationRoute(
                currentRoute = Screen.Notifications,
                onBackClick = {
                    navController.popBackStack()

                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                }


            )
        }
        composable<Screen.Profile> {
            ProfileScreenRoute(
                currentRoute = Screen.Profile,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home)
                },
                onNavigateToProfile = {

                }
            )



        }
    }
}