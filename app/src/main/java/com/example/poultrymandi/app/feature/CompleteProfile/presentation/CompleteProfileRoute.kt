package com.example.poultrymandi.app.feature.CompleteProfile.presentation

import androidx.compose.runtime.Composable

@Composable
fun CompleteProfileRoute(
    onProfileCompleted: () -> Unit
) {
    CompleteProfileScreen(
        onProfileCompleted = onProfileCompleted
    )
}
