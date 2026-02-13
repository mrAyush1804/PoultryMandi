package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

class LoginValidationUseCase {

    /**
     * Email validation logic
     * - Email empty na ho
     * - Valid email format ho
     * - Length check
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> {
                ValidationResult.Error("Email required hai")
            }
            email.length < 5 -> {
                ValidationResult.Error("Email minimum 5 characters hona chahiye")
            }
            !isValidEmailFormat(email) -> {
                ValidationResult.Error("Valid email address daal")
            }
            else -> ValidationResult.Success
        }
    }

    /**
     * Password validation logic
     * - Password empty na ho
     * - Minimum 8 characters
     * - Strong password check (optional)
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> {
                ValidationResult.Error("Password required hai")
            }
            password.length < 8 -> {
                ValidationResult.Error("Password minimum 8 characters hona chahiye")
            }
            !isStrongPassword(password) -> {
                ValidationResult.Error("Password mein uppercase, number aur special char hona chahiye")
            }
            else -> ValidationResult.Success
        }
    }

    /**
     * Pura form validate karo
     */
    fun validateLoginForm(email: String, password: String): LoginValidationResult {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return LoginValidationResult(
            emailValidation = emailValidation,
            passwordValidation = passwordValidation,
            isFormValid = emailValidation is ValidationResult.Success &&
                    passwordValidation is ValidationResult.Success
        )
    }


    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        return emailPattern.matches(email)
    }

    private fun isStrongPassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    }
}