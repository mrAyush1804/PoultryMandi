package com.example.poultrymandi.app.feature.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.R
import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.domain.data.*
import com.example.poultrymandi.app.feature.home.domain.repository.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private var ratesJob: Job? = null
    private var companyRatesJob: Job? = null

    init {
        loadInitialData()
    }

    /**
     * Helper to get today's date in yyyy-MM-dd format to match Firestore doc IDs.
     * This ensures Company Rates always reflect live data regardless of DateSelector.
     */
    private fun getTodayDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.DateSelected -> onDateSelected(event.date)
            is HomeScreenEvent.CategorySelected -> onCategorySelected(event.category)
            is HomeScreenEvent.LanguageSelected -> onLanguageSelected(event.language)
            is HomeScreenEvent.StateSelected -> onStateSelected(event.state)
            is HomeScreenEvent.CityClicked -> onCityClick(event.marketRate)
            HomeScreenEvent.DynamicIslandClosed -> closeDynamicIsland()
            else -> {}
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val dates = repository.getLast7Days()
            val today = dates.lastOrNull()

            val dummyCategories = listOf(
                CategoryDomain("broiler", "Broiler", R.drawable.chicken),
                CategoryDomain("chicken", "Chicken", R.drawable.boiled_chicken),
                CategoryDomain("eggs", "Eggs", R.drawable.egg_tongue_face),
            )

            _uiState.update { 
                it.copy(
                    dates = dates,
                    selectedDate = today,
                    categories = dummyCategories,
                    selectedCategory = dummyCategories[2] // Default to Broiler
                )
            }

            today?.let { 
                fetchRatesForDate(it.isAvailable1)
                // Pre-load company rates for today (default city or state handled in fetchRatesForDate snapshot)
                fetchCompanyUpdates(getTodayDateString(), "")
            }
        }
    }

    private fun fetchRatesForDate(dateString: String) {
        Log.d("HomeViewModel", "Fetching rates for: $dateString")
        ratesJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }

        ratesJob = repository.getRatesForDate(dateString)
            .onEach { states ->
                _uiState.update { currentState ->
                    val newState = states.find { s -> s.name == currentState.selectedState?.name } ?: states.firstOrNull()
                    
                    // Auto-fetch company updates for the first city of the selected state using TODAY's date
                    newState?.cities?.firstOrNull()?.let { firstCity ->
                        fetchCompanyUpdates(getTodayDateString(), firstCity.city)
                    }

                    currentState.copy(
                        isLoading = false,
                        states = states,
                        selectedState = newState,
                        selectedCityRate = newState?.cities?.firstOrNull()
                    )
                }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to fetch rates") }
            }
            .launchIn(viewModelScope)
    }

    private fun onDateSelected(date: DataItem) {
        _uiState.update { it.copy(selectedDate = date) }
        fetchRatesForDate(date.isAvailable1)
    }

    private fun onCategorySelected(category: CategoryDomain) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    private fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    private fun onStateSelected(state: StateDomain) {
        val firstCity = state.cities.firstOrNull()
        _uiState.update {
            it.copy(
                selectedState = state,
                selectedCityRate = firstCity,
            )
        }
        
        if (firstCity != null) {

            fetchCompanyUpdates(getTodayDateString(), firstCity.city)
        }
    }

    private fun onCityClick(marketRate: MarketRateDomain) {
        _uiState.update {
            it.copy(
                selectedCityRate = marketRate,
               /* showDynamicIsland = true*/
            )
        }
        // Fix: Always use TODAY's date for company rates
        fetchCompanyUpdates(getTodayDateString(), marketRate.city)
    }

    private fun fetchCompanyUpdates(dateString: String, cityId: String) {
        if (dateString.isBlank()) return
        
        companyRatesJob?.cancel()
        companyRatesJob = repository.getCompanyRatesForCity(dateString, cityId)
            .onEach { updates ->
                _uiState.update { it.copy(historicalRateData = updates) }
            }
            .catch { e ->
                Log.e("HomeViewModel", "Error in company rates: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    private fun closeDynamicIsland() {
        _uiState.update { it.copy(showDynamicIsland = false) }
    }
}
