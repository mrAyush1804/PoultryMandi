package com.example.poultrymandi.app.feature.auth.presentation.singup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.feature.Splash.presentation.SplashScreen
import com.example.poultrymandi.app.feature.auth.presentation.signup.SignupScreen


@Composable
fun SignUpRoute(
    viewModel: SingupViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: (userId: String) -> Unit

) {

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.singupSuccess) {
        if (uiState.singupSuccess) {
            onSignUpSuccess("newUser123")
        }
    }

    SignupScreen(
        viewModel = viewModel,
        onLoginClick = { onNavigateToLogin },
        onSignupSuccess = { onSignUpSuccess("newUser123") },

    )


}