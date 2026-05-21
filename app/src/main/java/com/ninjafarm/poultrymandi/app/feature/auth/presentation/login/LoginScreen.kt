package com.ninjafarm.poultrymandi.app.feature.auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninjafarm.poultrymandi.app.Core.ui.components.AppButton
import com.ninjafarm.poultrymandi.app.Core.ui.components.AppEditText
import com.ninjafarm.poultrymandi.app.Core.ui.components.CustomInputType
import com.ninjafarm.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.ninjafarm.poultrymandi.app.Core.ui.theme.brown
import com.ninjafarm.poultrymandi.R

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
                                .clickable {
                                    onEvent(LoginEvent.ClearError)
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
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = brown
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (uiState.isForgotLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color       = Color(0xFF2ECC71)
                    )
                } else {
                    Text(
                        text     = "Forgot Password?",
                        color    = Color(0xFF2ECC71),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            onEvent(
                                LoginEvent.ForgotPasswordClicked(uiState.email)
                            )
                        }
                    )
                }
            }

            // Forgot Password success message
            AnimatedVisibility(visible = uiState.forgotPasswordSent) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape  = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Text(
                        text     = "✅ Password reset link bhej diya!\nEmail check karo aur naya password set karo.",
                        color    = Color(0xFF2ECC71),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Forgot Password error message
            AnimatedVisibility(visible = uiState.forgotPasswordError != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape  = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Text(
                        text     = uiState.forgotPasswordError ?: "",
                        color    = Color(0xFFEF5350),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
        }
    }
}
