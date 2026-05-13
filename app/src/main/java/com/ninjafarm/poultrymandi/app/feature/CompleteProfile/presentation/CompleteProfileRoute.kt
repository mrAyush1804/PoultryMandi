package com.ninjafarm.poultrymandi.app.feature.CompleteProfile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CompleteProfileRoute(
    onProfileCompleted: () -> Unit,
    viewModel: CompleteProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.submissionSuccess) {
        if (uiState.submissionSuccess) {
            onProfileCompleted()
        }
    }

    CompleteProfileScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
