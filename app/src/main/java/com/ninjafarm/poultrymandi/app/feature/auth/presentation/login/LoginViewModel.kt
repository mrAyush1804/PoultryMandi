package com.ninjafarm.poultrymandi.app.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.LoginUseCase
import com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.validation.LoginValidationUseCase
import com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginValidationUseCase: LoginValidationUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> updateEmail(event.email)
            is LoginEvent.PasswordChanged -> updatePassword(event.password)
            is LoginEvent.RememberMeChanged -> updateRememberMe(event.checked)
            LoginEvent.LoginClicked -> performLogin()
            LoginEvent.ClearError -> clearError()
        }
    }

    private fun updateEmail(email: String) {
        val emailValidation = loginValidationUseCase.validateEmail(email)
        val emailError = when (emailValidation) {
            is ValidationResult.Error -> emailValidation.message
            ValidationResult.Success -> null
        }
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError,
            isFormValid = checkFormValidity(email, _uiState.value.password)
        )
    }

    private fun updatePassword(password: String) {
        val passwordValidation = loginValidationUseCase.validatePassword(password)
        val passwordError = when (passwordValidation) {
            is ValidationResult.Error -> passwordValidation.message
            ValidationResult.Success -> null
        }
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            generalError = null,
            isFormValid = checkFormValidity(_uiState.value.email, password)
        )
    }

    private fun updateRememberMe(checked: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = checked)
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

        // Step 1 — Form validate karo
        val formValidation = loginValidationUseCase.validateLoginForm(
            email = currentState.email,
            password = currentState.password
        )

        if (!formValidation.isFormValid) {
            _uiState.value = currentState.copy(
                emailError = when (formValidation.emailValidation) {
                    is ValidationResult.Error ->
                        (formValidation.emailValidation as ValidationResult.Error).message

                    ValidationResult.Success -> null
                },
                passwordError = when (formValidation.passwordValidation) {
                    is ValidationResult.Error ->
                        (formValidation.passwordValidation as ValidationResult.Error).message

                    ValidationResult.Success -> null
                }
            )
            return
        }

        // Step 2 — Loading start karo
        _uiState.value = currentState.copy(
            isLoading = true,
            generalError = null
        )

        viewModelScope.launch {
            val result = loginUseCase(
                email = currentState.email,
                password = currentState.password
            )

            result.onSuccess { userId ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    userId = userId,
                    successMessage = "Login successful! ✅",
                    generalError = null
                )
            }.onFailure { exception ->
                val errorMsg = when {


                    exception.message?.contains("PASSWORD_RESET_SENT") == true ->
                        "Aapke email pe password set karne ka link bheja gaya hai ✅\n" +
                                "Email check karo, naya password set karo, phir login karo."


                    exception.message?.contains("Wrong password", ignoreCase = true) == true ->
                        "Password galat hai, dobara try karo 🔐"


                    exception.message?.contains("Account nahi mila", ignoreCase = true) == true ->
                        "Account nahi mila"


                    exception.message?.contains("blocked", ignoreCase = true) == true ||
                            exception.message?.contains("too many", ignoreCase = true) == true ->
                        "Zyada attempts ho gaye, thodi der baad try karo "


                    exception.message?.contains("network", ignoreCase = true) == true ->
                        "Internet connection check karo "

                    else -> exception.message ?: "Login failed, dobara try karo"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    generalError = errorMsg
                )
            }
        }
    }
}

