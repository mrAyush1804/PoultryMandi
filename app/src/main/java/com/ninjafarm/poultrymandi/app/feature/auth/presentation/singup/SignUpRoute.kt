package com.ninjafarm.poultrymandi.app.feature.auth.presentation.singup

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninjafarm.poultrymandi.app.feature.auth.presentation.signup.SignupScreen

@Composable
fun SignUpRoute(
    viewModel            : SingupViewModel = hiltViewModel(),
    onNavigateToLogin    : () -> Unit = {},
    onGoogleSignUpSuccess: () -> Unit = {},  // ✅ Google → SetPassword
    onManualSignUpSuccess: () -> Unit = {},  // ✅ Manual → CompleteProfile
    webClientId          : String
) {
    val uiState by viewModel.uiState.collectAsState()

    // ✅ Google Sign In success → SetPassword screen
    LaunchedEffect(uiState.googleSignInSuccess) {
        if (uiState.googleSignInSuccess) {
            onGoogleSignUpSuccess()
        }
    }

    // ✅ Manual signup success → CompleteProfile screen
    LaunchedEffect(uiState.singupSuccess) {
        if (uiState.singupSuccess) {
            onManualSignUpSuccess()
        }
    }

    SignupScreen(
        viewModel       = viewModel,
        onLoginClick    = onNavigateToLogin,
        onSignupSuccess = onGoogleSignUpSuccess,
        webClientId     = webClientId
    )
}
