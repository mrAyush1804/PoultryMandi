package com.example.poultrymandi.app.feature.auth.domain.usecase.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}