package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}