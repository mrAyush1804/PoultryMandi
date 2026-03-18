package com.example.poultrymandi.app.feature.home.domain.data

data class CompanyRate(
    val companyName: String,
    val rate: Double,
    val previousRate: Double,
    val productionType: String, // e.g., "Standard", "Premium"
    val lastUpdated: String, // e.g., "2h ago", "15m ago"
    val category: String // "BROILER", "EGGS", "CHICK"
) {
    val trend: PriceTrend
        get() = when {
            rate > previousRate -> PriceTrend.UP
            rate < previousRate -> PriceTrend.DOWN
            else -> PriceTrend.STABLE
        }
}

data class MarketRateDomain(
    val city: String = "",
    val companyRates: List<CompanyRate> = emptyList(),

    // Broiler prices
    val todayPrice: Double = 0.0,
    val yesterdayPrice: Double = 0.0,

    // Eggs prices
    val eggsPrice: Double = 0.0,
    val eggsYesterday: Double = 0.0,

    // Chicken prices
    val chickenPrice: Double = 0.0,
    val chickenYesterday: Double = 0.0,

    val lastUpdatedAtLabel: String = "Just now", // NEW: For UI display
    val tomorrowPrice: Double? = null,
    val dayAfterTomorrowPrice: Double? = null,
    val unit: String = "PER KG",
    val trend: PriceTrend = PriceTrend.STABLE
) {
    val priceChange: Double get() = todayPrice - yesterdayPrice

    // Get price by category
    fun getPriceForCategory(category: String): Double = when (category.lowercase()) {
        "eggs" -> eggsPrice
        "chicken" -> chickenPrice
        else -> todayPrice  // broiler default
    }

    fun getYesterdayPriceForCategory(category: String): Double = when (category.lowercase()) {
        "eggs" -> eggsYesterday
        "chicken" -> chickenYesterday
        else -> yesterdayPrice
    }

    fun getPriceChangeForCategory(category: String): Double =
        getPriceForCategory(category) - getYesterdayPriceForCategory(category)
}

enum class PriceTrend {
    UP, DOWN, STABLE
}

data class StateDomain(
    val name: String,
    val cities: List<MarketRateDomain>
)
