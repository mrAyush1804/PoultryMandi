package com.example.poultrymandi.app.feature.CompleteProfile.presentation

data class CompleteProfileState(
    val name: String = "",
    val nameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val occupation: String = "",
    val occupationError: String? = null,
    val income: String = "",
    val incomeError: String? = null,
    val address: String = "",
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val submissionSuccess: Boolean = false,
    val generalError: String? = null
)

sealed class CompleteProfileEvent {
    data class NameChanged(val name: String) : CompleteProfileEvent()
    data class PhoneNumberChanged(val phone: String) : CompleteProfileEvent()
    data class OccupationChanged(val occupation: String) : CompleteProfileEvent()
    data class IncomeChanged(val income: String) : CompleteProfileEvent()
    data class AddressChanged(val address: String) : CompleteProfileEvent()
    object SubmitClicked : CompleteProfileEvent()
}
