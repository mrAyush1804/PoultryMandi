package com.example.poultrymandi.app.feature.auth.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppEditText
import com.example.poultrymandi.app.Core.ui.components.CustomInputType
import com.example.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.example.poultrymandi.app.Core.ui.theme.BlackC
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.auth.presentation.singup.SingupEvent
import com.example.poultrymandi.app.feature.auth.presentation.singup.SingupViewModel


@Composable
fun SignupScreen(
    viewModel: SingupViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onSignupSuccess: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    var contactInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()


    val isPhone = uiState.email.all { it.isDigit() } && uiState.email.isNotEmpty()
    val currentInputType = if (isPhone) CustomInputType.Phone else CustomInputType.Email

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.chicks),
                contentDescription = "App Logo",
                modifier = Modifier.size(70.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Join Poultry Mandi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Create an account to start trading",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.generalError != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.generalError ?: "",
                            color = Color(0xFFC62828),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFC62828),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.onEvent(SingupEvent.ClearError)
                                }
                        )
                    }
                }
            }

            // 1. Full Name Field
            AppEditText(
                value = uiState.name,
                onValueChange = { name ->
                 viewModel.onEvent(SingupEvent.NameChanged(name))
                },
                label = "Full Name",
                placeholder = "Enter your full name",
                errorMessage = uiState.nameError,
                isError = uiState.nameError != null,
                inputType = CustomInputType.Name,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = brown)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Email/Phone Field (Dynamic)
            AppEditText(
                value = uiState.email,
                onValueChange = {
                 viewModel.onEvent(SingupEvent.EmailChanged(it))
                },
                label = if (isPhone) "Phone Number" else "Email or Phone",
                placeholder = if (isPhone) "9876543210" else "example@gmail.com",
                inputType = currentInputType,
                errorMessage = uiState.emailError,
                isError = uiState.emailError != null,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(
                        imageVector = if (isPhone) Icons.Default.Phone else Icons.Default.Email,
                        contentDescription = null,
                        tint = brown
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Occupation Field
            AppEditText(
                value = uiState.occupation,
                onValueChange = {
                  viewModel.onEvent(SingupEvent.OccupationChanged(it))

                },
                label = "Occupation",
                placeholder = "Farmer, Trader, or Retailer?",
                inputType = CustomInputType.Text,
                errorMessage = uiState.occupationError,
                isError = uiState.occupationError != null,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = brown)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Password Field
            AppEditText(
                value = uiState.password,
                onValueChange = {
                    viewModel.onEvent(SingupEvent.PasswordChanged(it))
                },
                label = "Password",
                placeholder = "Create a strong password",
                inputType = CustomInputType.Password,
                errorMessage = uiState.passwordError,
                isError = uiState.passwordError != null,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = brown)
                },
                trailingIcon = {
                    val imageRes = if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(painter = painterResource(id = imageRes), contentDescription = null, tint = brown)
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))


            AppEditText(
                value = uiState.confirmPassword,
                onValueChange = {
                    viewModel.onEvent(SingupEvent.ConfirmPasswordChanged(it))
                },
                label = "Confirm Password",
                placeholder = "Create a strong password",
                inputType = CustomInputType.Password,
                errorMessage = uiState.confirmPasswordError,
                isError = uiState.confirmPasswordError != null,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = brown)
                },
                trailingIcon = {
                    val imageRes = if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(painter = painterResource(id = imageRes), contentDescription = null, tint = brown)
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))


            AppButton(
                text = "Create Account",
                onClick = {
                    viewModel.onEvent(SingupEvent.SignupClicked)
                },
                enabled = uiState.isFormValid && !uiState.isLoading,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(text = "OR", style = MaterialTheme.typography.labelMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))


            AppButton(
                text = "Signup with Google",
                onClick = { /* Google Auth Logic */ },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {

                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = BlackC)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Footer
            TextButton(onClick = { onLoginClick() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account? ", color = Color.Black)
                    Text("Login", color = Color.Blue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}





