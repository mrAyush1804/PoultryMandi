package com.ninjafarm.poultrymandi.app.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginRoute (
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: (userId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess(uiState.userId)
        }
    }

    LoginScreen(
        onEvent = { event ->
            viewModel.onEvent(event)
        },
        onLoginSuccess = {},
        onSignupClick = onNavigateToSignUp,
        uiState = uiState
    )
}