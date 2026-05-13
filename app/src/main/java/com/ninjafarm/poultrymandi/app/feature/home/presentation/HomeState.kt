package com.ninjafarm.poultrymandi.app.feature.home.presentation

import com.ninjafarm.poultrymandi.app.feature.home.data.model.DataItem
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.CategoryDomain
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.StateDomain

data class HomeState(
    val dates: List<DataItem> = emptyList(),
    val selectedDate: DataItem? = null,
    val categories: List<CategoryDomain> = emptyList(),
    val selectedCategory: CategoryDomain? = null,
    val states: List<StateDomain> = emptyList(),
    val selectedState: StateDomain? = null,
    val historicalRateData: List<CompanyRateUpdate> = emptyList(),
    val groupedCompanyRates: Map<String, List<CompanyRateUpdate>> = emptyMap(),
    val expandedCityId: String? = null,
    val showDynamicIsland: Boolean = false,
    val selectedCityRate: MarketRateDomain? = null,
    val selectedLanguage: String = "Eng",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null,
)

sealed class HomeScreenEvent {
    data class DateSelected(val date: DataItem) : HomeScreenEvent()
    data class CategorySelected(val category: CategoryDomain) : HomeScreenEvent()
    data class StateSelected(val state: StateDomain) : HomeScreenEvent()
    data class CitySelected(val marketRate: MarketRateDomain) : HomeScreenEvent()
    data class CityClicked(val marketRate: MarketRateDomain) : HomeScreenEvent()
    object DynamicIslandClosed : HomeScreenEvent()
    data class LanguageSelected(val language: String) : HomeScreenEvent()
}
