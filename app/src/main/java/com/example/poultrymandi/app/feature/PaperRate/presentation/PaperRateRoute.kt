package com.example.poultrymandi.app.feature.PaperRate.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.Core.navigation.Screen

@Composable
fun PaperRateRoute(
    viewModel: PaperRateViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    currentRoute: Screen = Screen.PaperRate,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    PaperRateScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRefresh = { viewModel.fetchPaperRates() },
        onNavigateToHome = onNavigateToHome,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToNotifications = onNavigateToNotifications
    )
}