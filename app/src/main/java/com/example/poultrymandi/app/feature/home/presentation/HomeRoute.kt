package com.example.poultrymandi.app.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun HomeRoute(
    viewmodel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {

    val uiState by viewmodel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {

        }
    }

    HomeScreen(
        state = uiState,
        onEvent = { event ->
            viewmodel.onEvent(event)
        },
        onLoginClick = onNavigateToLogin,
        onSignUpClick = onNavigateToSignUp,


    )






}