package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

data class SingupValidationResult(
    val nameValidation: ValidationResult,
    val emailValidation: ValidationResult,
    val phoneValidation: ValidationResult,
    val occupationValidation: ValidationResult,
    val passwordValidation: ValidationResult,
    val confirmPasswordValidation: ValidationResult,
    val isFormValid: Boolean
) {
}