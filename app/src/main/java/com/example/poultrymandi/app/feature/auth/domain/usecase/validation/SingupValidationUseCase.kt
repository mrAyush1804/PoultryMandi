package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




class SingupValidationUseCase @Inject constructor() {



    /**
     * Name validate karo
     * FieldValidator.validateName() use kar rahe ho
     */
    fun validateName(name: String): ValidationResult {
        return FieldValidator.validateName(name)
    }

    fun validateOccupation(occupation: String): ValidationResult {
        return FieldValidator.validateOccupation(occupation)
    }


    /**
     * Email validate karo
     * FieldValidator.validateEmail() use kar rahe ho
     *
     * Same function jo LoginValidationUseCase use karta hai!
     * Reusable ✓
     */
    fun validateEmail(email: String): ValidationResult {
        return FieldValidator.validateEmail(email)
    }

    /**
     * Phone validate karo
     * FieldValidator.validatePhone() use kar rahe ho
     */
    fun validatePhone(phone: String): ValidationResult {
        return FieldValidator.validatePhone(phone)
    }

    /**
     * Password validate karo
     * FieldValidator.validatePassword() use kar rahe ho
     *
     * Same function jo LoginValidationUseCase use karta hai!
     * Reusable ✓
     */
    fun validatePassword(password: String): ValidationResult {
        return FieldValidator.validatePassword(password)
    }

    /**
     * Confirm password validate karo
     * FieldValidator.validateConfirmPassword() use kar rahe ho
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return FieldValidator.validateConfirmPassword(password, confirmPassword)
    }

    /**
     * Pura signup form validate karo
     * Sab fields ko check karo
     */
    fun validateSignupForm(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): SingupValidationResult {
        val nameValidation = validateName(name)
        val emailValidation = validateEmail(email)
        val phoneValidation = validatePhone(phone)
        val passwordValidation = validatePassword(password)
        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)

        return SingupValidationResult(
            nameValidation = nameValidation,
            emailValidation = emailValidation,
            phoneValidation = phoneValidation,
            passwordValidation = passwordValidation,
            confirmPasswordValidation = confirmPasswordValidation,
            isFormValid = listOf(
                nameValidation,
                emailValidation,
                phoneValidation,
                passwordValidation,
                confirmPasswordValidation
            ).all { it is ValidationResult.Success }
        )
    }

    /**
     * Check karo ki pura form valid hai ya nahi
     */
    fun isSignupFormValid(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return validateSignupForm(name, email, phone, password, confirmPassword).isFormValid
    }


}