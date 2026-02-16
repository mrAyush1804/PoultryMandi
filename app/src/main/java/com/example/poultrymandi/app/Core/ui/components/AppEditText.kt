package com.example.poultrymandi.app.Core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.poultrymandi.app.Core.ui.theme.brown


sealed class CustomInputType {
    object Text : CustomInputType()
    object Number : CustomInputType()
    object Email : CustomInputType()
    object Password : CustomInputType()
    object Phone : CustomInputType()
    object Name : CustomInputType()
}

@Immutable
data class CustomTextFieldState(
    val borderWidth: Dp = 1.dp,
    val borderColor: Color = Color.Unspecified,
    val focusedBorderColor: Color = Color.Unspecified,
    val errorBorderColor: Color = Color.Unspecified,
    val shape: Shape = Shapes().medium,  // Default shape
    val backgroundColor: Color = Color.Unspecified,
    val gradientBackground: Int? = null,
    val gradientBrush: Brush? = null
) {
    companion object {

        val DefaultShapes = Shapes()
    }
}
@Composable
fun AppEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: CustomTextFieldState = CustomTextFieldState(),
    inputType: CustomInputType = CustomInputType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {

    val keyboardType = when (inputType) {
        CustomInputType.Text -> KeyboardType.Text
        CustomInputType.Number -> KeyboardType.Number
        CustomInputType.Email -> KeyboardType.Email
        CustomInputType.Password -> KeyboardType.Password
        CustomInputType.Phone -> KeyboardType.Phone
        CustomInputType.Name -> KeyboardType.Text
    }

    val borderColor = if (isError) state.errorBorderColor else state.borderColor

    Column(modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = state.gradientBrush ?: Brush.verticalGradient(
                        listOf(state.backgroundColor, state.backgroundColor)
                    ),
                    shape = state.shape
                )
                .border(state.borderWidth, borderColor, state.shape)
        ) {

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                label = label?.let { { Text(it) } },
                placeholder = placeholder?.let { { Text(it) } },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                isError = isError,
                singleLine = singleLine,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}


@Composable
fun AppEditTextPreview() {

    var textState by remember { mutableStateOf("") }

    AppEditText(
        value = textState,
        onValueChange = { textState= it },
        label = "Mobile Number",
        placeholder = "Enter phone number",
        state = CustomTextFieldState(
            borderColor = brown
        )
    )
}
