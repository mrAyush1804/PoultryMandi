package com.example.poultrymandi.app.feature.home.domain.data

data class MarketRateDomain(
    val city: String,
    val todayPrice: Double,
    val yesterdayPrice: Double,
    val tomorrowPrice: Double? = null,
    val dayAfterTomorrowPrice: Double? = null,
    val unit: String = "PER PIECE",
    val trend: PriceTrend = PriceTrend.STABLE
) {
    val priceChange: Double
        get() = todayPrice - yesterdayPrice
}

enum class PriceTrend {
    UP, DOWN, STABLE
}

data class StateDomain(
    val name: String,
    val cities: List<MarketRateDomain>
)
