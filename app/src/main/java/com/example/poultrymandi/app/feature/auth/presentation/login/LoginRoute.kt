package com.example.poultrymandi.app.feature.auth.presentation.login
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
            onLoginSuccess("user123")
        }
    }

    LoginScreen(
        onEvent = { event ->
            viewModel.onEvent(event)
        },
        onLoginSuccess = {
            onLoginSuccess("user123")
        },
        onSignupClick = onNavigateToSignUp,
        uiState = uiState
    )
}