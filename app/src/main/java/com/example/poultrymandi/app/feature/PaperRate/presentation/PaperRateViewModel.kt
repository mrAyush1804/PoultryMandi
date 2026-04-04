package com.example.poultrymandi.app.feature.PaperRate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.PaperRate.domain.model.PaperRate
import com.example.poultrymandi.app.feature.PaperRate.domain.repository.PaperRateRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PaperRateViewModel @Inject constructor(
    private val repository: PaperRateRepository  // ✅ Repository inject karo
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaperRateUiState>(PaperRateUiState.Loading)
    val uiState: StateFlow<PaperRateUiState> = _uiState.asStateFlow()

    init {
        fetchPaperRates()
    }

    fun fetchPaperRates() {
        viewModelScope.launch {
            _uiState.value = PaperRateUiState.Loading
            repository.getPaperRates()  // ✅ Supabase se aayega
                .collect { rates ->
                    _uiState.value = if (rates.isEmpty()) {
                        PaperRateUiState.Empty
                    } else {
                        PaperRateUiState.Success(rates)
                    }
                }
        }
    }
}