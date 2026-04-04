package com.example.poultrymandi.app.feature.PaperRate.presentation

import com.example.poultrymandi.app.feature.PaperRate.domain.model.PaperRate

sealed class PaperRateUiState {
    object Loading : PaperRateUiState()
    data class Success(val rates: List<PaperRate>) : PaperRateUiState()
    data class Error(val message: String) : PaperRateUiState()
    object Empty : PaperRateUiState()
}