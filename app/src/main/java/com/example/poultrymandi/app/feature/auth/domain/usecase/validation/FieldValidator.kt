// File: com.example.poultrymandi.app.feature.auth.domain.usecase.validation.FieldValidator.kt

package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

/**
 * REUSABLE FIELD VALIDATORS
 *
 * Ye object (singleton) hai jo sab validation functions contain karta hai
 * Kisi bhi screen mein use kar sakte ho
 *
 * Login, Signup, Profile Update, Password Change - sab jagah same validators use ho sakte hain
 */

object FieldValidator {

    // ==================== EMAIL VALIDATION ====================
    /**
     * Email validate karo
     * - Empty check
     * - Format check
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
                ValidationResult.Error("Valid email address daal (example@domain.com)")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== PASSWORD VALIDATION ====================
    /**
     * Password validate karo
     * - Empty check
     * - Minimum length
     * - Strong password check
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
                ValidationResult.Error(
                    "Password strong hona chahiye:\n" +
                            "• Uppercase (A-Z)\n" +
                            "• Lowercase (a-z)\n" +
                            "• Number (0-9)\n" +
                            "• Special char (!@#$%)"
                )
            }
            else -> ValidationResult.Success
        }
    }

    /**
     * Simple password - sirf length check (agar strong password na chahiye)
     */
    fun validateSimplePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> {
                ValidationResult.Error("Password required hai")
            }
            password.length < 6 -> {
                ValidationResult.Error("Password minimum 6 characters hona chahiye")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== PHONE VALIDATION ====================
    /**
     * Phone number validate karo (India - 10 digits)
     * - Empty check
     * - Exactly 10 digits
     * - Sirf numbers
     */
    fun validatePhone(phone: String): ValidationResult {
        return when {
            phone.isBlank() -> {
                ValidationResult.Error("Phone number required hai")
            }
            phone.length != 10 -> {
                ValidationResult.Error("Phone number exactly 10 digits hona chahiye")
            }
            !phone.all { it.isDigit() } -> {
                ValidationResult.Error("Phone number mein sirf digits (0-9) ho sakte hain")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== NAME VALIDATION ====================
    /**
     * Name validate karo
     * - Empty check
     * - Minimum length
     * - Maximum length
     * - Sirf letters aur space
     */
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> {
                ValidationResult.Error("Name required hai")
            }
            name.length < 3 -> {
                ValidationResult.Error("Name minimum 3 characters hona chahiye")
            }
            name.length > 50 -> {
                ValidationResult.Error("Name maximum 50 characters ho sakta hai")
            }
            !name.all { it.isLetter() || it.isWhitespace() } -> {
                ValidationResult.Error("Name mein sirf letters aur space ho sakte hain")
            }
            else -> ValidationResult.Success
        }
    }

    fun validateOccupation(occupation: String): ValidationResult {
        return when {
            occupation.isBlank() -> {
                ValidationResult.Error("Occupation required hai")
            }
            else -> ValidationResult.Success
        }
    }


    // ==================== CONFIRM PASSWORD VALIDATION ====================
    /**
     * Dono passwords match kar rahe hain ya nahi check karo
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> {
                ValidationResult.Error("Confirm password required hai")
            }
            password != confirmPassword -> {
                ValidationResult.Error("Passwords match nahi kar rahe")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== USERNAME VALIDATION ====================
    /**
     * Username validate karo
     * - Minimum 3 characters
     * - Maximum 20 characters
     * - Letters, numbers, underscore, dash
     */
    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> {
                ValidationResult.Error("Username required hai")
            }
            username.length < 3 -> {
                ValidationResult.Error("Username minimum 3 characters hona chahiye")
            }
            username.length > 20 -> {
                ValidationResult.Error("Username maximum 20 characters ho sakta hai")
            }
            !username.matches(Regex("^[a-zA-Z0-9_.-]+$")) -> {
                ValidationResult.Error("Username mein sirf letters, numbers, _, . - ho sakte hain")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== NUMBER VALIDATION ====================
    /**
     * Number validate karo
     * - Empty check
     * - Numeric check
     * - Min/Max range check
     */
    fun validateNumber(
        number: String,
        min: Int? = null,
        max: Int? = null
    ): ValidationResult {
        return when {
            number.isBlank() -> {
                ValidationResult.Error("Number required hai")
            }
            !number.all { it.isDigit() } -> {
                ValidationResult.Error("Valid number daal")
            }
            min != null && number.toIntOrNull() != null && number.toInt() < min -> {
                ValidationResult.Error("Number minimum $min hona chahiye")
            }
            max != null && number.toIntOrNull() != null && number.toInt() > max -> {
                ValidationResult.Error("Number maximum $max ho sakta hai")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== PIN CODE VALIDATION (INDIA) ====================
    /**
     * Pin code validate karo (6 digits)
     */
    fun validatePinCode(pinCode: String): ValidationResult {
        return when {
            pinCode.isBlank() -> {
                ValidationResult.Error("Pin code required hai")
            }
            pinCode.length != 6 -> {
                ValidationResult.Error("Pin code exactly 6 digits hona chahiye")
            }
            !pinCode.all { it.isDigit() } -> {
                ValidationResult.Error("Pin code mein sirf digits ho sakte hain")
            }
            else -> ValidationResult.Success
        }
    }



    // ==================== TEXT FIELD VALIDATION (GENERIC) ====================
    /**
     * Generic text field validate karo
     * - Empty check
     * - Length check
     */
    fun validateTextField(
        text: String,
        fieldName: String,
        minLength: Int = 1,
        maxLength: Int = Int.MAX_VALUE
    ): ValidationResult {
        return when {
            text.isBlank() -> {
                ValidationResult.Error("$fieldName required hai")
            }
            text.length < minLength -> {
                ValidationResult.Error("$fieldName minimum $minLength characters hona chahiye")
            }
            text.length > maxLength -> {
                ValidationResult.Error("$fieldName maximum $maxLength characters ho sakta hai")
            }
            else -> ValidationResult.Success
        }
    }

    // ==================== HELPER FUNCTIONS ====================

    /**
     * Email format check karo
     */
    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        return emailPattern.matches(email)
    }

    /**
     * Password strong hai ya nahi check karo
     * Requirements:
     * - At least 1 uppercase letter
     * - At least 1 lowercase letter
     * - At least 1 digit
     * - At least 1 special character
     */
    private fun isStrongPassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    }
}