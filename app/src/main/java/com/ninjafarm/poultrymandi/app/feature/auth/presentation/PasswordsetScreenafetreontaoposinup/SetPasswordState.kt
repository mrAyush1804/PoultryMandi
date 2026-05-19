package com.ninjafarm.poultrymandi.app.feature.auth.presentation.PasswordsetScreenafetreontaoposinup

data class SetPasswordState(
    val password                 : String  = "",
    val confirmPassword          : String  = "",
    val passwordError            : String? = null,
    val confirmPasswordError     : String? = null,
    val generalError             : String? = null,
    val successMessage           : String? = null,
    val isLoading                : Boolean = false,
    val isSuccess                : Boolean = false,
    val isPasswordVisible        : Boolean = false,
    val isConfirmPasswordVisible : Boolean = false,
    val isFormValid              : Boolean = false
)