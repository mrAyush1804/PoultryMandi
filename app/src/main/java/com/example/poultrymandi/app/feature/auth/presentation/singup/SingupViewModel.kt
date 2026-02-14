package com.example.poultrymandi.app.feature.auth.presentation.singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.SingupValidationUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SingupViewModel @Inject constructor(
    private val singupValidationUseCase: SingupValidationUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(SingupState())
    val uiState: StateFlow<SingupState> = _uiState.asStateFlow()


    fun onEvent(event: SingupEvent) {
        when (event) {

            is SingupEvent.EmailChanged -> {
                updateEmail(event.email)

            }

            is SingupEvent.PasswordChanged -> {
                updatePassword(event.password)
            }

            is SingupEvent.NameChanged -> {
                updateName(event.name)
            }

            is SingupEvent.ContactChanged -> {
                updateContact(event.contact)
            }

            is SingupEvent.OccupationChanged -> {
                updateoccupation(event.occupation)
            }

            SingupEvent.SignupClicked -> {
                performSignup()
            }

            SingupEvent.ClearError -> {
                clearError()
            }


        }
    }

    private fun updateoccupation(occupation: String) {
        val occupationValidation = singupValidationUseCase.validateOccupation(occupation)

        val occupationError = when (occupationValidation) {
            is ValidationResult.Error -> occupationValidation.message
            ValidationResult.Success -> null

        }

        _uiState.value=_uiState.value.copy(
            occupation = occupation,
            occupationError = occupationError,
            isFormValid = checkFormValidity(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.name,
                occupation

            )
        )
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(generalError = null)
    }


    private fun updateName(name: String) {
        val nameValidation = singupValidationUseCase.validateName(name)

        val nameError = when (nameValidation) {
            is ValidationResult.Error -> nameValidation.message
            ValidationResult.Success -> null
        }
        _uiState.value = _uiState.value.copy(
            name = name, nameError = nameError, isFormValid = checkFormValidity(
                _uiState.value.email, _uiState.value.password, name, _uiState.value.contact
            )
        )

    }

    private fun updateContact(contact: String) {
        val contactValidation = singupValidationUseCase.validatePhone(contact)
        val contactError = when (contactValidation) {
            is ValidationResult.Error -> contactValidation.message
            ValidationResult.Success -> null

        }
        _uiState.value = _uiState.value.copy(
            contact = contact, contactError = contactError, isFormValid = checkFormValidity(
                _uiState.value.email, _uiState.value.password, _uiState.value.name, contact
            )
        )

    }

    private fun updatePassword(password: String) {
        val passwordValidation = singupValidationUseCase.validatePassword(password)
        val passwordError = when (passwordValidation) {
            is ValidationResult.Error -> passwordValidation.message
            ValidationResult.Success -> null
        }
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            generalError = null,
            isFormValid =
                checkFormValidity(
                    _uiState.value.email,
                    password,
                    _uiState.value.name,
                    _uiState.value.contact
                )
        )


    }

    private fun updateEmail(email: String) {
        val emailValidation = singupValidationUseCase.validateEmail(email)
        val emailError = when (emailValidation) {
            is ValidationResult.Error -> emailValidation.message
            ValidationResult.Success -> null
        }


        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError,
            generalError = null,
            isFormValid = checkFormValidity(
                email,
                _uiState.value.password,
                _uiState.value.name,
                _uiState.value.contact
            )

        )

    }

    private fun checkFormValidity(
        email: String, password: String, name: String, contact: String
    ): Boolean {
        val validation = singupValidationUseCase.validateSignupForm(
            name = name,
            email = email,
            phone = contact,
            password = password,
            confirmPassword = password
        )
        return validation.isFormValid
    }


    private fun performSignup() {

        val currentState = _uiState.value

        val formValidation = singupValidationUseCase.validateSignupForm(
            name = currentState.name,
            email = currentState.email,
            phone = currentState.contact,
            password = currentState.password,
            confirmPassword = currentState.password
        )


        if (!formValidation.isFormValid) {
            _uiState.value = currentState.copy(
                emailError = when (formValidation.emailValidation) {
                    is ValidationResult.Error -> (formValidation.emailValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null

                },

                nameError = when (formValidation.nameValidation) {
                    is ValidationResult.Error -> (formValidation.nameValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null

                },

                contactError = when (formValidation.passwordValidation) {
                    is ValidationResult.Error -> (formValidation.passwordValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null

                },
                passwordError = when (formValidation.passwordValidation) {
                    is ValidationResult.Error -> (formValidation.passwordValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null

                },

                confirmPasswordError = when (formValidation.confirmPasswordValidation) {
                    is ValidationResult.Error -> (formValidation.confirmPasswordValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null


                }


            )
            return


        }

        _uiState.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(2000)

                _uiState.value = currentState.copy(
                    isLoading = false,
                    singupSuccess = true,
                    successMessage = "Signup successful!",
                    generalError = null
                )

            } catch (e: Exception) {

                _uiState.value = currentState.copy(
                    isLoading = false,
                    singupSuccess = false,
                    generalError = e.message ?: "Signup failed, try again"
                )


            }
        }


    }

}