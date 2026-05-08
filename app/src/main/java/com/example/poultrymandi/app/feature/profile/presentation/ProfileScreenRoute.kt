package com.example.poultrymandi.app.feature.profile.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.Core.navigation.Screen
import com.example.poultrymandi.app.feature.notification.presentation.NotificationScreen
import com.example.poultrymandi.app.feature.notification.presentation.NotificationViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreenRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    currentRoute: Screen = Screen.Profile,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPaperRate: () -> Unit
) {


    val logoutEvent by viewModel.logoutEvent.collectAsState()

    // ✅ When logout fires, navigate to Login screen
    LaunchedEffect(logoutEvent) {
        if (logoutEvent) {
            onNavigateToLogin()
        }
    }

    val uiState by viewModel.uiState.collectAsState()


        ProfileScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
        )






}