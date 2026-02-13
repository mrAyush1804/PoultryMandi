package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

data class SingupValidationResult(
    val emailValidation: ValidationResult,
    val passwordValidation: ValidationResult,
    val isFormValid: Boolean
) {
}