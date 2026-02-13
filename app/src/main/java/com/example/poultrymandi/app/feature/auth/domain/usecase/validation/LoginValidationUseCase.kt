package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

import javax.inject.Inject

class LoginValidationUseCase @Inject constructor(){


    // ==================== METHOD 1: EMAIL VALIDATION ====================
    /**
     * Email validate karo
     *
     * FieldValidator.validateEmail() ko call karta hai
     * Result: ValidationResult.Success or ValidationResult.Error
     *
     * Usage:
     * val result = loginValidationUseCase.validateEmail("user@example.com")
     * when (result) {
     *     ValidationResult.Success -> "Email valid hai"
     *     is ValidationResult.Error -> "Error: ${result.message}"
     * }
     */

    fun validateEmail(email: String): ValidationResult {

        return FieldValidator.validateEmail(email)

    }

    // ==================== METHOD 2: PASSWORD VALIDATION ====================
    /**
     * Password validate karo
     *
     * FieldValidator.validatePassword() ko call karta hai
     * Result: ValidationResult.Success or ValidationResult.Error
     *
     * Usage:
     * val result = loginValidationUseCase.validatePassword("Pass@123")
     * when (result) {
     *     ValidationResult.Success -> "Password valid hai"
     *     is ValidationResult.Error -> "Error: ${result.message}"
     * }
     */



    fun validatePassword(password: String): ValidationResult {
        // FieldValidator object se validatePassword method call karo
        return FieldValidator.validatePassword(password)
    }







    // ==================== METHOD 3: COMPLETE FORM VALIDATION ====================
    /**
     * Pura login form validate karo
     *
     * Dono fields (email aur password) ko check kara:
     * - Email validation
     * - Password validation
     * - Final isFormValid flag
     *
     * Returns: LoginValidationResult
     *
     * Usage:
     * val result = loginValidationUseCase.validateLoginForm(
     *     email = "user@example.com",
     *     password = "Pass@123"
     * )
     *
     * when {
     *     result.isFormValid -> "Form valid hai, login karo"
     *     result.emailValidation is ValidationResult.Error -> "Email invalid"
     *     result.passwordValidation is ValidationResult.Error -> "Password invalid"
     * }
     */



    fun validateLoginForm(
        email: String,
        password: String
    ): LoginValidationResult {

        // Step 1: Email ko validate karo
        val emailValidation = validateEmail(email)

        // Step 2: Password ko validate karo
        val passwordValidation = validatePassword(password)

        // Step 3: Check karo dono fields valid hain ya nahi
        val isFormValid = emailValidation is ValidationResult.Success &&
                passwordValidation is ValidationResult.Success

        // Step 4: Result return karo
        return LoginValidationResult(
            emailValidation = emailValidation,
            passwordValidation = passwordValidation,
            isFormValid = isFormValid
        )
    }


}