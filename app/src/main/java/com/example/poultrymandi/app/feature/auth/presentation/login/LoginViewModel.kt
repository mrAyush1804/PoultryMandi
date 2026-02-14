package com.example.poultrymandi.app.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.LoginValidationUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginValidationUseCase: LoginValidationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()


    fun onEvent(event: LoginEvent) {
        when (event) {

            is LoginEvent.EmailChanged -> {
                updateEmail(event.email)
            }

            // Password change event
            is LoginEvent.PasswordChanged -> {
                updatePassword(event.password)
            }

            // Remember me checkbox
            is LoginEvent.RememberMeChanged -> {
                updateRememberMe(event.checked)
            }

            // Login button click
            LoginEvent.LoginClicked -> {
                performLogin()
            }

            // Clear error message
            LoginEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun updateEmail(email: String) {

        val emailValidation = loginValidationUseCase.validateEmail(email)

        val emailError = when (emailValidation) {
            // Agar error tha to message extract karo
            is ValidationResult.Error -> emailValidation.message
            // Agar success tha to error null
            ValidationResult.Success -> null
        }

        // Step 3: UI state update karo
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError,
            isFormValid = checkFormValidity(email, _uiState.value.password)
        )

    }


    private fun updatePassword(password: String) {

        // Step 1: LoginValidationUseCase se password validate karo
        val passwordValidation = loginValidationUseCase.validatePassword(password)

        // Step 2: Validation result se error message extract karo
        val passwordError = when (passwordValidation) {
            // Agar error tha to message extract karo
            is ValidationResult.Error -> passwordValidation.message
            // Agar success tha to error null
            ValidationResult.Success -> null
        }

        // Step 3: UI state update karo
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            generalError = null, // Clear karo general error
            isFormValid = checkFormValidity(_uiState.value.email, password)
        )
    }


    private fun updateRememberMe(checked: Boolean) {
        _uiState.value = _uiState.value.copy(
            rememberMe = checked
        )
    }

    private fun checkFormValidity(email: String, password: String): Boolean {
        val validation = loginValidationUseCase.validateLoginForm(email, password)
        return validation.isFormValid
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(generalError = null)
    }

    private fun performLogin() {

        val currentState = _uiState.value

        // Step 1: Final form validation karo
        // LoginValidationUseCase.validateLoginForm() call hota hai ← YAHA USE HOTA HAI
        val formValidation = loginValidationUseCase.validateLoginForm(
            email = currentState.email,
            password = currentState.password
        )

        // Step 2: Form invalid hai to errors show karo aur return karo
        if (!formValidation.isFormValid) {
            _uiState.value = currentState.copy(
                // Email error show karo
                emailError = when (formValidation.emailValidation) {
                    is ValidationResult.Error ->
                        (formValidation.emailValidation as ValidationResult.Error).message

                    ValidationResult.Success -> null
                },
                // Password error show karo
                passwordError = when (formValidation.passwordValidation) {
                    is ValidationResult.Error ->
                        (formValidation.passwordValidation as ValidationResult.Error).message

                    ValidationResult.Success -> null
                }
            )
            return // Return karo, login mat karo
        }

        // Step 3: Form valid hai - loading start karo
        _uiState.value = currentState.copy(isLoading = true)


        viewModelScope.launch {
            try {


                kotlinx.coroutines.delay(2000)


                _uiState.value = currentState.copy(
                    isLoading = false,
                    loginSuccess = true,
                    successMessage = "Login successful!",
                    generalError = null
                )

            } catch (e: Exception) {

                _uiState.value = currentState.copy(
                    isLoading = false,
                    loginSuccess = false,
                    generalError = e.message ?: "Login failed, try again"
                )
            }
        }
    }


}