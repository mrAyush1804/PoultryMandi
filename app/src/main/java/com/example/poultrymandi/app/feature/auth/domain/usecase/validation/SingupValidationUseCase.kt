package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

import javax.inject.Inject

class SingupValidationUseCase @Inject constructor() {

    fun validateName(name: String): ValidationResult {
        return FieldValidator.validateName(name)
    }

    fun validateOccupation(occupation: String): ValidationResult {
        return FieldValidator.validateOccupation(occupation)
    }

    fun validateEmail(email: String): ValidationResult {
        return FieldValidator.validateEmail(email)
    }

    fun validatePhone(phone: String): ValidationResult {
        return FieldValidator.validatePhone(phone)
    }

    fun validatePassword(password: String): ValidationResult {
        return FieldValidator.validatePassword(password)
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        // Confirm password abhi type hi nahi kiya → koi error nahi
        if (confirmPassword.isBlank()) return ValidationResult.Success

        // Dono filled hain → ab check karo match karte hain ya nahi
        return if (password == confirmPassword) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Passwords match nahi kar rahe ")
        }
    }


    fun validateSignupForm(
        name: String,
        email: String,
        phone: String,
        occupation: String,
        password: String,
        confirmPassword: String
    ): SingupValidationResult {

        val nameValidation = validateName(name)
        val occupationValidation = validateOccupation(occupation)
        val passwordValidation = validatePassword(password)
        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)

        // ✅ CHANGE — Email OR Phone, jo bhi diya ho sirf usko validate karo
        val emailValidation = if (email.isNotBlank()) {
            validateEmail(email)        // email diya → validate karo
        } else {
            ValidationResult.Success    // email nahi diya → ignore karo
        }

        val phoneValidation = if (phone.isNotBlank()) {
            validatePhone(phone)        // phone diya → validate karo
        } else {
            ValidationResult.Success    // phone nahi diya → ignore karo
        }

        // ✅ CHANGE — Ek toh hona chahiye (dono blank nahi ho sakte)
        val hasEmailOrPhone = email.isNotBlank() || phone.isNotBlank()

        val isFormValid = hasEmailOrPhone &&
                nameValidation is ValidationResult.Success &&
                emailValidation is ValidationResult.Success &&
                phoneValidation is ValidationResult.Success &&
                occupationValidation is ValidationResult.Success &&
                passwordValidation is ValidationResult.Success &&
                confirmPasswordValidation is ValidationResult.Success

        return SingupValidationResult(
            nameValidation = nameValidation,
            emailValidation = emailValidation,
            phoneValidation = phoneValidation,
            occupationValidation = occupationValidation,
            passwordValidation = passwordValidation,
            confirmPasswordValidation = confirmPasswordValidation,
            isFormValid = isFormValid
        )
    }

    fun isSignupFormValid(
        name: String,
        email: String,
        phone: String,
        occupation: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return validateSignupForm(name, email, phone, occupation, password, confirmPassword).isFormValid
    }
}