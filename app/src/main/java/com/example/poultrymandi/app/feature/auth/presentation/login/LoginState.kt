package com.example.poultrymandi.app.feature.auth.presentation.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val loginSuccess: Boolean = false,
    val successMessage: String? = null,
    val rememberMe: Boolean= false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class RememberMeChanged(val checked: Boolean) : LoginEvent()
    object LoginClicked : LoginEvent()
    object ClearError : LoginEvent()
}