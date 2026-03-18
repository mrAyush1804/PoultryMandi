package com.example.poultrymandi.app.feature.auth.presentation.singup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.GoogleSignInUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.SendEmailLinkUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.SingupValidationUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SingupViewModel @Inject constructor(
    private val singupValidationUseCase: SingupValidationUseCase,
    private val sendEmailLinkUseCase: SendEmailLinkUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SingupState())
    val uiState: StateFlow<SingupState> = _uiState.asStateFlow()


    companion object {
        private const val TAG = "SingupViewModel"
    }


    fun onEvent(event: SingupEvent) {
        Log.d(TAG, "onEvent called: $event")
        when (event) {
            is SingupEvent.EmailChanged -> updateEmail(event.email)
            is SingupEvent.PasswordChanged -> updatePassword(event.password)
            is SingupEvent.NameChanged -> updateName(event.name)
            is SingupEvent.ContactChanged -> updateContact(event.contact)
            is SingupEvent.OccupationChanged -> updateOccupation(event.occupation)
            is SingupEvent.ConfirmPasswordChanged -> updateConfirmPassword(event.confirmPassword)
            SingupEvent.SignupClicked -> performSignup()
            SingupEvent.ClearError -> clearError()
            is SingupEvent.GoogleSignInClicked -> performGoogleSignIn(event.idToken)
                // Handle Google Sign-In click

        }
    }


    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { currentState ->
            val validation = singupValidationUseCase.validateConfirmPassword(
                currentState.password,
                confirmPassword
            )
            val error = if (validation is ValidationResult.Error) validation.message else null
            Log.d(TAG, "updateConfirmPassword → error: $error")
            currentState.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = error,
                isFormValid = checkBasicFormValidity(
                    name = currentState.name,
                    email = currentState.email,
                    occupation = currentState.occupation,
                    password = currentState.password,
                    confirmPassword = confirmPassword
                )
            )
        }
    }

    private fun performGoogleSignIn(idToken: String) {
        Log.d(TAG, "performGoogleSignIn → START")
        _uiState.update { it.copy(isGoogleLoading = true, generalError = null) }

        viewModelScope.launch {
            googleSignInUseCase(idToken)
                .onSuccess {
                    Log.d(TAG, "performGoogleSignIn → ✅ SUCCESS")
                    _uiState.update {
                        it.copy(
                            isGoogleLoading = false,
                            googleSignInSuccess = true
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "performGoogleSignIn → ❌ ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isGoogleLoading = false,
                            generalError = error.message ?: "Google sign in failed"
                        )
                    }
                }
        }
    }



    private fun updateOccupation(occupation: String) {
        _uiState.update { currentState ->
            val validation = singupValidationUseCase.validateOccupation(occupation)
            val error = if (validation is ValidationResult.Error) validation.message else null
            Log.d(TAG, "updateOccupation → error: $error")

            currentState.copy(
                occupation = occupation,
                occupationError = error,
                isFormValid = checkBasicFormValidity(
                    name = currentState.name,
                    email = currentState.email,
                    occupation = occupation,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword
                )
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update { currentState ->
            val validation = singupValidationUseCase.validateName(name)
            val error = if (validation is ValidationResult.Error) validation.message else null
            Log.d(TAG, "updateName → error: $error")
            currentState.copy(
                name = name,
                nameError = error,
                isFormValid = checkBasicFormValidity(
                    name = name,
                    email = currentState.email,
                    occupation = currentState.occupation,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword
                )
            )
        }
    }

    private fun updateContact(contact: String) {
        _uiState.update { currentState ->
            // Contact is checked separately but contributes to basic validity if needed
            // For now, keeping name/email/occ/pass as main drivers
            Log.d(TAG, "updateContact → contact: $contact")
            currentState.copy(
                contact = contact,
                isFormValid = checkBasicFormValidity(
                    name = currentState.name,
                    email = currentState.email,
                    occupation = currentState.occupation,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword
                )
            )
        }
    }

    private fun updatePassword(password: String) {
        _uiState.update { currentState ->
            val validation = singupValidationUseCase.validatePassword(password)
            val error = if (validation is ValidationResult.Error) validation.message else null
            Log.d(TAG, "updatePassword → error: $error")
            currentState.copy(
                password = password,
                passwordError = error,
                isFormValid = checkBasicFormValidity(
                    name = currentState.name,
                    email = currentState.email,
                    occupation = currentState.occupation,
                    password = password,
                    confirmPassword = currentState.confirmPassword
                )
            )
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update { currentState ->
            val validation = singupValidationUseCase.validateEmail(email)
            val error = if (validation is ValidationResult.Error) validation.message else null
            Log.d(TAG, "updateEmail → email: $email | error: $error")
            currentState.copy(
                email = email,
                emailError = error,
                isFormValid = checkBasicFormValidity(
                    name = currentState.name,
                    email = email,
                    occupation = currentState.occupation,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword
                )
            )
        }
    }

    /**
     * Simply checks if all required fields have text.
     * This allows the button to enable as soon as the user has filled the form.
     * Strict validation (regex, etc.) is handled via error messages and on final click.
     */
    private fun checkBasicFormValidity(
        name: String,
        email: String,
        occupation: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val contact = _uiState.value.contact

        // ✅ Email YA Phone — ek hona chahiye (dono nahi)
        val hasEmailOrPhone = email.isNotBlank() || contact.isNotBlank()

        val isValid = name.isNotBlank() &&
                hasEmailOrPhone &&               // ← OR logic
                occupation.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword

        Log.d(TAG, "checkBasicFormValidity → isValid: $isValid | email: $email | contact: $contact")
        return isValid
    }

    private fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }

    private fun performSignup() {
        Log.d(TAG, "performSignup → START")
        val currentState = _uiState.value
        Log.d(
            TAG,
            "performSignup → currentState: name=${currentState.name} email=${currentState.email} contact=${currentState.contact} occupation=${currentState.occupation}"
        )
        Log.d(TAG, "performSignup → formValidation.isFormValid:")


        // Full validation check before proceeding
        val formValidation = singupValidationUseCase.validateSignupForm(
            name = currentState.name,
            email = currentState.email,
            phone = currentState.contact,
            password = currentState.password,
            occupation = currentState.occupation,
            confirmPassword = currentState.confirmPassword
        )

        if (!formValidation.isFormValid) {
            Log.e(TAG, "performSignup → FORM INVALID, errors:")
            Log.e(TAG, "  emailError: ${formValidation.emailValidation}")
            Log.e(TAG, "  nameError: ${formValidation.nameValidation}")
            Log.e(TAG, "  phoneError: ${formValidation.phoneValidation}")
            Log.e(TAG, "  occupationError: ${formValidation.occupationValidation}")
            Log.e(TAG, "  passwordError: ${formValidation.passwordValidation}")
            Log.e(TAG, "  confirmPasswordError: ${formValidation.confirmPasswordValidation}")
            _uiState.update { state ->
                state.copy(
                    emailError = if (formValidation.emailValidation is ValidationResult.Error) formValidation.emailValidation.message else null,
                    nameError = if (formValidation.nameValidation is ValidationResult.Error) formValidation.nameValidation.message else null,
                    contactError = if (formValidation.phoneValidation is ValidationResult.Error) formValidation.phoneValidation.message else null,
                    occupationError = if (formValidation.occupationValidation is ValidationResult.Error) formValidation.occupationValidation.message else null,
                    passwordError = if (formValidation.passwordValidation is ValidationResult.Error) formValidation.passwordValidation.message else null,
                    confirmPasswordError = if (formValidation.confirmPasswordValidation is ValidationResult.Error) formValidation.confirmPasswordValidation.message else null
                )
            }

            return
        }
        Log.d(
            TAG,
            "performSignup → Form valid, calling sendEmailLinkUseCase with email: ${currentState.email}"
        )

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            Log.d(TAG, "performSignup → Coroutine started, sending email...")

            sendEmailLinkUseCase(currentState.email)
                .onSuccess {
                    Log.d(TAG, "performSignup → ✅ SUCCESS — Email link sent!")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            singupSuccess = true,
                            successMessage = "Verification link bhej diya! Email check karo ✅"
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "performSignup → ❌ FAILED — ${error.message}", error)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            singupSuccess = false,
                            generalError = error.message
                                ?: "Link bhejne mein error aaya, dobara try karo"
                        )
                    }
                }
        }
    }
}
