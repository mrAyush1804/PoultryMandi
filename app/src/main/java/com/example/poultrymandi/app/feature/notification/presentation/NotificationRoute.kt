package com.example.poultrymandi.app.feature.notification.presentation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.Core.navigation.Screen


@Composable
fun NotficationRoute(
    viewModel: NotificationViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    currentRoute: Screen = Screen.Notifications,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPaperRate: () -> Unit





    ) {


    val uiState by viewModel.uiState.collectAsState()

    NotificationScreen(
        viewModel = viewModel,
        onBackClick = onBackClick,
        onNavigateToProfile=onNavigateToProfile,
    )



}