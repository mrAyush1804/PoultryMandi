package com.example.poultrymandi.app.feature.auth.presentation.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppEditText
import com.example.poultrymandi.app.Core.ui.components.CustomInputType
import com.example.poultrymandi.app.Core.ui.components.CustomTextFieldState
import com.example.poultrymandi.app.Core.ui.theme.brown


@Preview(showBackground = true)
@Composable
fun LoginScreen() {

    var loginInput by remember { mutableStateOf("") }
    val isPhone = loginInput.all { it.isDigit() } && loginInput.isNotEmpty()
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val currentInputType = if (isPhone) CustomInputType.Phone else CustomInputType.Email
    val currentLabel = if (isPhone) "Phone Number" else "Email or Phone"
    val currentPlaceholder = if (isPhone) "9876543210" else "example@gmail.com"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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

            // Email Input
            AppEditText(
                value = loginInput,
                onValueChange = { loginInput = it },
                inputType = currentInputType,
                label = currentLabel,
                placeholder = currentPlaceholder,
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

            Spacer(modifier = Modifier.height(16.dp))


            AppEditText(
                value = password,
                onValueChange = { password = it },
                inputType = CustomInputType.Password,
                label = "Password",
                placeholder = "Enter your password",
                state = CustomTextFieldState(
                    borderColor = brown,
                    focusedBorderColor = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = brown)
                },

                trailingIcon = @androidx.compose.runtime.Composable {
                    val imageRes =
                        if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = imageRes),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = brown
                        )
                    }
                },

                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))


            AppButton(
                text = "Login Now",
                isLoading = isLoading,
                onClick = {

                    isLoading = true

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Text(
                        "Don't have an account?",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Register",
                        color = Color.Blue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
    }
}