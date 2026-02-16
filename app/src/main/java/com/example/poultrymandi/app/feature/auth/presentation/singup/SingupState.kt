package com.example.poultrymandi.app.feature.auth.presentation.singup

data class SingupState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val contact: String = "",
    val occupation: String = "",
    val occupationError: String? = null,
    val contactError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String?=null,
    val singupSuccess: Boolean = false,
    val successMessage: String? = null,
    val isSignupSuccess: Boolean = false

)

sealed class  SingupEvent{
    data class EmailChanged(val email: String) : SingupEvent()
    data class PasswordChanged(val password: String) : SingupEvent()
    data class NameChanged(val name: String) : SingupEvent()
    data class ContactChanged(val contact: String) : SingupEvent()

    data class OccupationChanged(val occupation: String) : SingupEvent()

    data class  ConfirmPasswordChanged(val confirmPassword: String) : SingupEvent()
    object SignupClicked : SingupEvent()
    object ClearError : SingupEvent()


}