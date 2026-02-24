package com.example.poultrymandi.app.feature.SubcriptionPackage.presentation

import com.example.poultrymandi.app.feature.SubcriptionPackage.domian.model.SubscriptionPlan

/**
 * State class for the Subscription screen.
 */
data class SubscriptionState(
    val languages: List<String> = listOf("English", "हिंदी", "मराठी"),
    val selectedLanguage: String = "English",
    val plans: List<SubscriptionPlan> = emptyList(),
    val selectedPlanId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)