package com.example.poultrymandi.app.feature.SubcriptionPackage.presentation

import androidx.lifecycle.ViewModel
import com.example.poultrymandi.app.feature.SubcriptionPackage.domian.model.SubscriptionPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for managing subscription logic and state.
 */
@HiltViewModel
class   SubscriptionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionState())
    val uiState: StateFlow<SubscriptionState> = _uiState.asStateFlow()

    init {
        loadPlans()
    }

    private fun loadPlans() {
        val dummyPlans = listOf(
            SubscriptionPlan("1m", "1 Month", "₹199"),
            SubscriptionPlan("3m", "3 Months", "₹499", tag = "Most Popular"),
            SubscriptionPlan("6m", "6 Months", "₹899"),
            SubscriptionPlan("12m", "12 Months", "₹1599", tag = "Best Value")
        )
        _uiState.update {
            it.copy(
                plans = dummyPlans,
                selectedPlanId = dummyPlans[1].id
            )
        }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun onPlanSelected(planId: String) {
        _uiState.update { it.copy(selectedPlanId = planId) }
    }
}