package com.example.poultrymandi.app.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.R
import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.domain.data.CategoryDomain
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import com.example.poultrymandi.app.feature.home.domain.data.PriceTrend
import com.example.poultrymandi.app.feature.home.domain.data.StateDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.DateSelected -> onDateSelected(event.date)
            is HomeScreenEvent.CategorySelected -> onCategorySelected(event.category)
            is HomeScreenEvent.LanguageSelected -> onLanguageSelected(event.language)
            is HomeScreenEvent.StateSelected -> onStateSelected(event.state)
            is HomeScreenEvent.CityClicked -> onCityClick(event.marketRate)
            HomeScreenEvent.DynamicIslandClosed -> closeDynamicIsland()

        }

    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val dummyDates = listOf(
                DataItem(11, "Mon", "September", 2023, "2023-09-11", isAvailable = true),
                DataItem(12, "Tue", "September", 2023, "2023-09-12", isAvailable = true),
                DataItem(13, "Wed", "September", 2023, "2023-09-13", isAvailable = true),
                DataItem(14, "Thu", "September", 2023, "2023-09-14", isAvailable = true),
                DataItem(15, "Fri", "September", 2023, "2023-09-15", isAvailable = true),
                DataItem(16, "Sat", "September", 2023, "2023-09-16", isAvailable = true),
            )

            val dummyCategories = listOf(
                CategoryDomain("eggs", "Eggs", R.drawable.egg_tongue_face),
                CategoryDomain("chicken", "Chicken", R.drawable.boiled_chicken),
                CategoryDomain("broiler", "Broiler", R.drawable.chicken),
            )

            // Madhya Pradesh Cities
            val mpCities = listOf(
                MarketRateDomain("Indore", 4.55, 4.50, 4.60, 4.65, trend = PriceTrend.UP),
                MarketRateDomain("Bhopal", 4.55, 4.60, 4.50, 4.45, trend = PriceTrend.DOWN),
                MarketRateDomain("Jabalpur", 4.50, 4.50, 4.50, 4.55, trend = PriceTrend.STABLE),
                MarketRateDomain("Gwalior", 4.65, 4.45, 4.70, 4.75, trend = PriceTrend.UP),
                MarketRateDomain("Ujjain", 4.40, 4.35, 4.45, 4.50, trend = PriceTrend.UP),
            )

            // Maharashtra Cities
            val mhCities = listOf(
                MarketRateDomain("Mumbai", 5.10, 5.00, 5.20, 5.25, trend = PriceTrend.UP),
                MarketRateDomain("Pune", 4.90, 4.95, 4.85, 4.80, trend = PriceTrend.DOWN),
                MarketRateDomain("Nagpur", 4.80, 4.80, 4.80, 4.85, trend = PriceTrend.STABLE),
                MarketRateDomain("Nashik", 4.70, 4.65, 4.75, 4.80, trend = PriceTrend.UP),
            )

            // Chhattisgarh Cities
            val cgCities = listOf(
                MarketRateDomain("Raipur", 4.60, 4.55, 4.65, 4.70, trend = PriceTrend.UP),
                MarketRateDomain("Bilaspur", 4.50, 4.55, 4.45, 4.40, trend = PriceTrend.DOWN),
                MarketRateDomain("Durg", 4.55, 4.55, 4.55, 4.60, trend = PriceTrend.STABLE),
            )

            // Gujarat Cities
            val gjCities = listOf(
                MarketRateDomain("Ahmedabad", 4.80, 4.70, 4.90, 4.95, trend = PriceTrend.UP),
                MarketRateDomain("Surat", 4.85, 4.90, 4.80, 4.75, trend = PriceTrend.DOWN),
                MarketRateDomain("Vadodara", 4.75, 4.75, 4.75, 4.80, trend = PriceTrend.STABLE),
                MarketRateDomain("Rajkot", 4.70, 4.60, 4.80, 4.85, trend = PriceTrend.UP),
            )

            val dummyStates = listOf(
                StateDomain("Madhya Pradesh", mpCities),
                StateDomain("Maharashtra", mhCities),
                StateDomain("Chhattisgarh", cgCities),
                StateDomain("Gujarat", gjCities)
            )

            _uiState.update { 
                it.copy(
                    isLoading = false,
                    dates = dummyDates,
                    selectedDate = dummyDates[2],
                    categories = dummyCategories,
                    selectedCategory = dummyCategories[0],
                    states = dummyStates,
                    selectedState = dummyStates[0]
                )
            }
        }
    }

    fun onDateSelected(date: DataItem) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onCategorySelected(category: CategoryDomain) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun onStateSelected(state: StateDomain) {
        _uiState.update { it.copy(selectedState = state, expandedCityId = null) }
    }

    fun onCityClick(marketRate: MarketRateDomain) {
        _uiState.update { 
            it.copy(
                selectedCityRate = marketRate,
                showDynamicIsland = true
            )
        }
    }

    fun closeDynamicIsland() {
        _uiState.update { it.copy(showDynamicIsland = false) }
    }
}
