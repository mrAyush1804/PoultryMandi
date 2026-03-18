package com.example.poultrymandi.app.feature.CompleteProfile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppEditText
import com.example.poultrymandi.app.Core.ui.components.CustomInputType
import com.example.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.example.poultrymandi.app.Core.ui.theme.brown

@Composable
fun CompleteProfileScreen(
    viewModel: CompleteProfileViewModel = hiltViewModel(),
    onProfileCompleted: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.submissionSuccess) {
        if (uiState.submissionSuccess) {
            onProfileCompleted()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            Image(
                painter = painterResource(id = R.drawable.chicks),
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Complete Your Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Help us personalize your market experience",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Full Name
            AppEditText(
                value = uiState.name,
                onValueChange = { viewModel.onEvent(CompleteProfileEvent.NameChanged(it)) },
                label = "Full Name",
                placeholder = "Enter your full name",
                errorMessage = uiState.nameError,
                isError = uiState.nameError != null,
                inputType = CustomInputType.Name,
                state = CustomTextFieldState(borderColor = brown, shape = MaterialTheme.shapes.medium),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = brown) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Phone Number
            AppEditText(
                value = uiState.phoneNumber,
                onValueChange = { viewModel.onEvent(CompleteProfileEvent.PhoneNumberChanged(it)) },
                label = "Phone Number",
                placeholder = "10 digit mobile number",
                errorMessage = uiState.phoneNumberError,
                isError = uiState.phoneNumberError != null,
                inputType = CustomInputType.Phone,
                state = CustomTextFieldState(borderColor = brown, shape = MaterialTheme.shapes.medium),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = brown) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Occupation
            AppEditText(
                value = uiState.occupation,
                onValueChange = { viewModel.onEvent(CompleteProfileEvent.OccupationChanged(it)) },
                label = "Occupation",
                placeholder = "e.g. Farmer, Trader, Feed Supplier",
                errorMessage = uiState.occupationError,
                isError = uiState.occupationError != null,
                inputType = CustomInputType.Text,
                state = CustomTextFieldState(borderColor = brown, shape = MaterialTheme.shapes.medium),
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = brown) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Annual Income
            AppEditText(
                value = uiState.income,
                onValueChange = { viewModel.onEvent(CompleteProfileEvent.IncomeChanged(it)) },
                label = "Annual Income (₹)",
                placeholder = "Approx annual income",
                errorMessage = uiState.incomeError,
                isError = uiState.incomeError != null,
                inputType = CustomInputType.Number,
                state = CustomTextFieldState(borderColor = brown, shape = MaterialTheme.shapes.medium),
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = brown) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Address (Optional)
            AppEditText(
                value = uiState.address,
                onValueChange = { viewModel.onEvent(CompleteProfileEvent.AddressChanged(it)) },
                label = "Full Address (Optional)",
                placeholder = "Village, Tehsil, District, State",
                inputType = CustomInputType.Text,
                state = CustomTextFieldState(borderColor = brown, shape = MaterialTheme.shapes.medium),
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = brown) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            AppButton(
                text = "Save and Continue",
                onClick = { viewModel.onEvent(CompleteProfileEvent.SubmitClicked) },
                enabled = uiState.isFormValid && !uiState.isLoading,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
