package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.validation

data class LoginValidationResult(
    val emailValidation: ValidationResult,
    val passwordValidation: ValidationResult,
    val isFormValid: Boolean
)