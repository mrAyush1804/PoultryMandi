package com.example.poultrymandi.app.feature.home.presentation.Navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.poultrymandi.app.Core.navigation.BottomNavScreen
import com.example.poultrymandi.app.Core.navigation.Screen
import com.example.poultrymandi.app.Core.ui.components.PoultryBottomBar


@Preview(showBackground = true)
@Composable
fun HomeBottomNavigationScreenHolder() {

    val navController = rememberNavController()

    val bottomScreens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Notifications,
        BottomNavScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val currentScreen: Screen? = when {
        currentRoute?.contains(Screen.Home::class.qualifiedName ?: "") == true -> Screen.Home
        currentRoute?.contains(Screen.Notifications::class.qualifiedName ?: "") == true -> Screen.Notifications
        currentRoute?.contains(Screen.Profile::class.qualifiedName ?: "") == true -> Screen.Profile
        else -> Screen.Home
    }

    var isBottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            PoultryBottomBar(
                screens = bottomScreens,
                currentRoute = currentScreen,
                isVisible = isBottomBarVisible,
                containerColor = Color.White,
                onNavigationSelected = { screen ->
                    val targetRoute = when (screen) {
                        is BottomNavScreen.Home -> Screen.Home
                        is BottomNavScreen.Notifications -> Screen.Notifications
                        is BottomNavScreen.Profile -> Screen.Profile
                        else -> Screen.Home
                    }

                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        BottomNavGraph(
            navController = navController,
            paddingValues = paddingValues,
            onBottomBarVisibilityChanged = { visible ->
                isBottomBarVisible = visible
            }
        )
    }
}