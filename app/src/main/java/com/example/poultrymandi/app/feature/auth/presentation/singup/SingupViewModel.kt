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

            is SingupEvent.ConfirmPasswordChanged -> {
                updateConfirmPassword(event.confirmPassword)
            }


            SingupEvent.SignupClicked -> {
                performSignup()
            }

            SingupEvent.ClearError -> {
                clearError()
            }


        }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        val confirmPasswordValidation = singupValidationUseCase.validateConfirmPassword(
            password = _uiState.value.password,
            confirmPassword = confirmPassword
        )

        val confirmPasswordError = when (confirmPasswordValidation) {
            is ValidationResult.Error -> confirmPasswordValidation.message
            ValidationResult.Success -> null

        }

        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = confirmPasswordError,
            isFormValid = checkFormValidity(
                name = _uiState.value.name,
                email = _uiState.value.email,
                contact = _uiState.value.contact,
                occupation = _uiState.value.occupation,
                password = _uiState.value.password,
                confirmPassword = confirmPassword
            )


        )

    }

    private fun updateoccupation(occupation: String) {
        val occupationValidation = singupValidationUseCase.validateOccupation(occupation)

        val occupationError = when (occupationValidation) {
            is ValidationResult.Error -> occupationValidation.message
            ValidationResult.Success -> null

        }

        _uiState.value = _uiState.value.copy(
            occupation = occupation,
            occupationError = occupationError,
            isFormValid = checkFormValidity(
                name = _uiState.value.name,
                email = _uiState.value.email,
                contact = _uiState.value.contact,
                password = _uiState.value.password,
                confirmPassword = _uiState.value.confirmPassword
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
            name = name,
            nameError = nameError,
            isFormValid = checkFormValidity(
                name = name,
                email = _uiState.value.email,
                contact = _uiState.value.contact,
                occupation = _uiState.value.occupation,
                password = _uiState.value.password,
                confirmPassword = _uiState.value.confirmPassword
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
            contact = contact,
            contactError = contactError,
            isFormValid = checkFormValidity(
                name = _uiState.value.name,
                email = _uiState.value.email,
                contact = contact,
                occupation = _uiState.value.occupation,
                password = _uiState.value.password,
                confirmPassword = _uiState.value.confirmPassword
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
            isFormValid = checkFormValidity(
                name = _uiState.value.name,
                email = _uiState.value.email,
                contact = _uiState.value.contact,
                occupation = _uiState.value.occupation,
                password = password,
                confirmPassword = _uiState.value.confirmPassword
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
                name = _uiState.value.name,
                email = email,
                contact = _uiState.value.contact,
                occupation = _uiState.value.occupation,
                password = _uiState.value.password,
                confirmPassword = _uiState.value.confirmPassword
            )

        )

    }

    private fun checkFormValidity(
        name: String = _uiState.value.name,
        email: String = _uiState.value.email,
        contact: String = _uiState.value.contact,
        occupation: String = _uiState.value.occupation,
        password: String = _uiState.value.password,
        confirmPassword: String = _uiState.value.confirmPassword
    ): Boolean {
        return singupValidationUseCase.validateSignupForm(
            name = name,
            email = email,
            phone = contact,
            password = password,
            occupation = _uiState.value.occupation,
            confirmPassword = confirmPassword
        ).isFormValid


    }


    private fun performSignup() {

        val currentState = _uiState.value

        val formValidation = singupValidationUseCase.validateSignupForm(
            name = currentState.name,
            email = currentState.email,
            phone = currentState.contact,
            password = currentState.password,
            occupation = currentState.occupation,
            confirmPassword = currentState.confirmPassword
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

                contactError = when (formValidation.phoneValidation) {
                    is ValidationResult.Error -> (formValidation.phoneValidation as ValidationResult.Error).message
                    ValidationResult.Success -> null

                },
                occupationError =when(formValidation.occupationValidation) {
                    is ValidationResult.Error -> (formValidation.occupationValidation as ValidationResult.Error).message
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