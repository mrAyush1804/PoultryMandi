package com.example.poultrymandi.app.feature.auth.presentation.login

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
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppEditText
import com.example.poultrymandi.app.Core.ui.components.CustomInputType
import com.example.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.example.poultrymandi.app.Core.ui.theme.brown




@Composable
fun LoginScreen(
    uiState: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onLoginSuccess: () -> Unit = {},
    onSignupClick: () -> Unit = {},
) {

    var isPasswordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val isPhone = uiState.email.all { it.isDigit() } && uiState.email.isNotEmpty()
    val currentInputType = if (isPhone) CustomInputType.Phone else CustomInputType.Email
    val currentLabel = if (isPhone) "Phone Number" else "Email Address"
    val currentPlaceholder = if (isPhone) "9876543210" else "example@gmail.com"

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
            Image(
                painter = painterResource(R.drawable.chicks),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Poultry Mandi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Login to your account",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                                .clickable { onEvent(LoginEvent.ClearError)
                                }
                        )
                    }
                }
            }

            AppEditText(
                value = uiState.email,
                onValueChange = { email ->
                    onEvent(LoginEvent.EmailChanged(email))
                },
                label = currentLabel,
                placeholder = currentPlaceholder,
                inputType = currentInputType,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    backgroundColor = Color.White,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(
                        imageVector = if (isPhone) Icons.Default.Phone else Icons.Default.Email,
                        contentDescription = null,
                        tint = brown,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppEditText(
                value = uiState.password,
                onValueChange = { password ->
                    onEvent(LoginEvent.PasswordChanged(password))
                },
                label = "Password",
                placeholder = "Enter your password",
                inputType = CustomInputType.Password,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    backgroundColor = Color.White,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = brown,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    val imageRes = if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = imageRes), // painterResource use karein
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = brown // Color yahan se control hoga
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.rememberMe,
                    onCheckedChange = { checked ->
                        onEvent(LoginEvent.RememberMeChanged(checked))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = brown,
                        uncheckedColor = brown
                    ),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Remember me",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Login Now",
                onClick = {
                    onEvent(LoginEvent.LoginClicked)
                },
                enabled = uiState.isFormValid && !uiState.isLoading,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onSignupClick() }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Don't have an account?",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Register",
                        color = Color.Blue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            if (uiState.loginSuccess) {
                LaunchedEffect(Unit) {
                    onLoginSuccess()
                }
            }
        }
    }
}