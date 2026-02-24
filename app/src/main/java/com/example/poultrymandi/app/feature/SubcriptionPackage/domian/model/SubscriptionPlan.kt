package com.example.poultrymandi.app.feature.SubcriptionPackage.domian.model

/**
 * Data model representing a subscription plan.
 */
data class SubscriptionPlan(
    val id: String,
    val duration: String,
    val price: String,
    val tag: String? = null,
    val features: List<String> = listOf(
        "Daily Egg Rates",
        "Broiler Market Trends",
        "Direct Mandi Prices"
    )
)