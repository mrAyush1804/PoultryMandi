package com.example.poultrymandi.app.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.Core.navigation.Screen
import com.example.poultrymandi.app.feature.notification.presentation.NotificationScreen
import com.example.poultrymandi.app.feature.notification.presentation.NotificationViewModel

@Composable
fun ProfileScreenRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    currentRoute: Screen = Screen.Profile,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPaperRate: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()


        ProfileScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
        )




}