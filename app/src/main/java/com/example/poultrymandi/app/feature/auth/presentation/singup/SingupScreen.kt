package com.example.poultrymandi.app.feature.auth.presentation.signup

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
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppEditText
import com.example.poultrymandi.app.Core.ui.components.CustomInputType
import com.example.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.example.poultrymandi.app.Core.ui.theme.BlackC
import com.example.poultrymandi.app.Core.ui.theme.brown

@Preview(showBackground = true)
@Composable
fun SignupScreen(
    onLoginClick: () -> Unit = {},
    onSignupSuccess: () -> Unit = {}
) {
    // States (ViewModel integrate karne par ye uiState se aayengi)
    var name by remember { mutableStateOf("") }
    var contactInput by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Dynamic Logic for Contact (Email/Phone)
    val isPhone = contactInput.all { it.isDigit() } && contactInput.isNotEmpty()
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
                modifier = Modifier.size(80.dp),
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

            // 1. Full Name Field
            AppEditText(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                placeholder = "Enter your full name",
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
                value = contactInput,
                onValueChange = { contactInput = it },
                label = if (isPhone) "Phone Number" else "Email or Phone",
                placeholder = if (isPhone) "9876543210" else "example@gmail.com",
                inputType = currentInputType,
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
                value = occupation,
                onValueChange = { occupation = it },
                label = "Occupation",
                placeholder = "Farmer, Trader, or Retailer?",
                inputType = CustomInputType.Text,
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
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Create a strong password",
                inputType = CustomInputType.Password,
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

            // Main Signup Button
            AppButton(
                text = "Create Account",
                onClick = { /* ViewModel Call */ },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Divider or "OR"
            Text(text = "OR", style = MaterialTheme.typography.labelMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Google Signup Button
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


