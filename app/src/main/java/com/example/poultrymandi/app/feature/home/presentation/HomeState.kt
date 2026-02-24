package com.example.poultrymandi.app.feature.home.presentation

import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.domain.data.CategoryDomain
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import com.example.poultrymandi.app.feature.home.domain.data.StateDomain

data class HomeState(
    val dates: List<DataItem> = emptyList(),
    val selectedDate: DataItem? = null,
    val categories: List<CategoryDomain> = emptyList(),
    val selectedCategory: CategoryDomain? = null,
    val states: List<StateDomain> = emptyList(),
    val selectedState: StateDomain? = null,
    val expandedCityId: String? = null,
    val showDynamicIsland: Boolean = false,
    val selectedCityRate: MarketRateDomain? = null,
    val selectedLanguage: String = "Eng",
    val isLoading: Boolean = false,
    val error: String? = null
)
