package com.example.poultrymandi.app.feature.CompleteProfile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.SaveProfileUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.SingupValidationUseCase
import com.example.poultrymandi.app.feature.auth.domain.usecase.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteProfileViewModel @Inject constructor(
    private val validationUseCase: SingupValidationUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,

    ) : ViewModel() {

    companion object { private const val TAG = "CompleteProfileVM" }


    private val _uiState = MutableStateFlow(CompleteProfileState())
    val uiState: StateFlow<CompleteProfileState> = _uiState.asStateFlow()

    fun onEvent(event: CompleteProfileEvent) {
        when (event) {
            is CompleteProfileEvent.NameChanged -> updateName(event.name)
            is CompleteProfileEvent.PhoneNumberChanged -> updatePhone(event.phone)
            is CompleteProfileEvent.OccupationChanged -> updateOccupation(event.occupation)
            is CompleteProfileEvent.IncomeChanged -> updateIncome(event.income)
            is CompleteProfileEvent.AddressChanged -> updateAddress(event.address)
            CompleteProfileEvent.SubmitClicked -> submitProfile()
        }
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(
            name = name,
            isFormValid = checkBasicValidity(name, it.phoneNumber, it.occupation, it.income)
        ) }
    }

    private fun updatePhone(phone: String) {
        _uiState.update { it.copy(
            phoneNumber = phone,
            isFormValid = checkBasicValidity(it.name, phone, it.occupation, it.income)
        ) }
    }

    private fun updateOccupation(occupation: String) {
        _uiState.update { it.copy(
            occupation = occupation,
            isFormValid = checkBasicValidity(it.name, it.phoneNumber, occupation, it.income)
        ) }
    }

    private fun updateIncome(income: String) {
        _uiState.update { it.copy(
            income = income,
            isFormValid = checkBasicValidity(it.name, it.phoneNumber, it.occupation, income)
        ) }
    }

    private fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    private fun checkBasicValidity(name: String, phone: String, occupation: String, income: String): Boolean {
        return name.isNotBlank() && phone.isNotBlank() && occupation.isNotBlank() && income.isNotBlank()
    }

    private fun submitProfile() {
        Log.d(TAG, "submit → START")
        val state = _uiState.value

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            saveProfileUseCase(
                name = state.name,
                phone = state.phoneNumber,
                occupation = state.occupation,
                income = state.income,
                address = state.address
            ).onSuccess {
                Log.d(TAG, "✅ Profile saved!")
                _uiState.update { it.copy(isLoading = false, submissionSuccess = true) }
            }.onFailure { error ->
                Log.e(TAG, "❌ Failed: ${error.message}")
                _uiState.update { it.copy(isLoading = false, generalError = error.message) }
            }
        }
    }
}
