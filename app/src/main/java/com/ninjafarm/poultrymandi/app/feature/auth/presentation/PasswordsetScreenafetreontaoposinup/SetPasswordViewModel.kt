package com.ninjafarm.poultrymandi.app.feature.auth.presentation.PasswordsetScreenafetreontaoposinup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SetPasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetPasswordState())
    val uiState: StateFlow<SetPasswordState> = _uiState.asStateFlow()

    private val TAG = "SetPasswordViewModel"

    fun onEvent(event: SetPasswordEvent) {
        when (event) {
            is SetPasswordEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
                validatePassword()
                checkFormValid()
            }
            is SetPasswordEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
                validateConfirmPassword()
                checkFormValid()
            }
            SetPasswordEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            SetPasswordEvent.ToggleConfirmPasswordVisibility -> {
                _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }
            SetPasswordEvent.SaveClicked -> savePassword()
            SetPasswordEvent.ClearError -> {
                _uiState.update { it.copy(generalError = null) }
            }
        }
    }

    private fun validatePassword() {
        val password = _uiState.value.password
        val error = when {
            password.isBlank() ->
                "Password required hai"
            password.length < 8 ->
                "Password minimum 8 characters ka hona chahiye"
            !password.any { it.isDigit() } ->
                "Password mein kam se kam 1 number hona chahiye (0-9)"
            !password.any { it.isLowerCase() } ->
                "Password mein kam se kam 1 small letter hona chahiye (a-z)"
            !password.any { it.isUpperCase() } ->
                "Password mein kam se kam 1 capital letter hona chahiye (A-Z)"
            !password.any { !it.isLetterOrDigit() } ->
                "Password mein kam se kam 1 special character hona chahiye (!@#$%)"
            else -> null
        }
        _uiState.update { it.copy(passwordError = error) }
    }

    private fun validateConfirmPassword() {
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        val error = when {
            confirmPassword.isBlank() -> "Password confirm karein"
            password != confirmPassword -> "Passwords match nahi kar rahe"
            else -> null
        }
        _uiState.update { it.copy(confirmPasswordError = error) }
    }

    private fun checkFormValid() {
        val state = _uiState.value
        val isValid = state.passwordError == null && 
                      state.confirmPasswordError == null &&
                      state.password.isNotBlank() &&
                      state.confirmPassword.isNotBlank()
        _uiState.update { it.copy(isFormValid = isValid) }
    }

    private fun savePassword() {
        validatePassword()
        validateConfirmPassword()
        checkFormValid()

        if (!_uiState.value.isFormValid) return

        _uiState.update { it.copy(isLoading = true, generalError = null, successMessage = null) }

        viewModelScope.launch {
            try {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    user.updatePassword(_uiState.value.password).await()
                    Log.d(TAG, "Password updated successfully")
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isSuccess = true,
                            successMessage = "Password successfully set! ✅"
                        ) 
                    }
                } else {
                    Log.e(TAG, "User null hai")
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            generalError = "User session expire ho gaya. Phir se login karein."
                        ) 
                    }
                }
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                Log.e(TAG, "Recent login required", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        generalError = "Security wajah se, please ek baar phir se login karke try karein."
                    ) 
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating password", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        generalError = e.localizedMessage ?: "Password update nahi ho paya. Dobara try karein."
                    ) 
                }
            }
        }
    }
}
