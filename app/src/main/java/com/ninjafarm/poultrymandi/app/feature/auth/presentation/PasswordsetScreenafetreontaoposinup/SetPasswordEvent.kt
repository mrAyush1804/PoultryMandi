package com.ninjafarm.poultrymandi.app.feature.auth.presentation.PasswordsetScreenafetreontaoposinup

sealed class SetPasswordEvent {
    data class PasswordChanged(val password: String) : SetPasswordEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SetPasswordEvent()
    object TogglePasswordVisibility : SetPasswordEvent()
    object ToggleConfirmPasswordVisibility : SetPasswordEvent()
    object SaveClicked : SetPasswordEvent()
    object ClearError : SetPasswordEvent()
}
