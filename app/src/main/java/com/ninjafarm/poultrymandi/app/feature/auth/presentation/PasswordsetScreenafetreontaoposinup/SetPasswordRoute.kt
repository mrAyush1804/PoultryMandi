package com.ninjafarm.poultrymandi.app.feature.auth.presentation.PasswordsetScreenafetreontaoposinup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SetPasswordRoute(
    viewModel: SetPasswordViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onSkip: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top-right: Skip Button
        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp)
        ) {
            Text(
                text = "Skip",
                color = Color(0xFF2ECC71),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Center: Screen Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SetPasswordScreen(
                viewModel = viewModel,
                onSuccess = onSuccess
            )
        }
    }
}
