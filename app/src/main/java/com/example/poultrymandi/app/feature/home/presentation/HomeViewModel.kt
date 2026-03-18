package com.example.poultrymandi.app.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.R
import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.domain.data.*
import com.example.poultrymandi.app.feature.home.domain.repository.RatesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RatesRepository,

) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private var ratesJob: Job? = null
    private var companyRatesJob: Job? = null

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
            // ── DATE SECTION ──────────────────────────
            // Generates last 7 days. Today is auto-selected (last item).
            val dates = repository.getLast7Days()
            val today = dates.lastOrNull()

            val dummyCategories = listOf(
                CategoryDomain("eggs", "Eggs", R.drawable.egg_tongue_face),
                CategoryDomain("chicken", "Chicken", R.drawable.boiled_chicken),
                CategoryDomain("broiler", "Broiler", R.drawable.chicken),
            )

            _uiState.update { 
                it.copy(
                    dates = dates,
                    selectedDate = today,
                    categories = dummyCategories,
                    selectedCategory = dummyCategories[2] // Default to Broiler
                )
            }

            today?.let { fetchRatesForDate(it.isAvailable1) }
        }
    }

    private fun fetchRatesForDate(dateString: String) {
        ratesJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }

        // ── STATES SECTION ────────────────────────
        // Real-time listener on rates/{date}/states sub-collection.
        ratesJob = repository.getRatesForDate(dateString)
            .onEach { states ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        states = states,
                        selectedState = states.find { s -> s.name == it.selectedState?.name } ?: states.firstOrNull()
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
        _uiState.update { it.copy(selectedState = state) }
    }

    private fun onCityClick(marketRate: MarketRateDomain) {
        _uiState.update { 
            it.copy(
                selectedCityRate = marketRate,
                showDynamicIsland = true
            )
        }
        
        // ── COMPANY RATES / DYNAMIC ISLAND ────────
        // Fetched on city click. Real-time updates for history.
        val dateString = _uiState.value.selectedDate?.isAvailable1 ?: ""
        fetchCompanyUpdates(dateString, marketRate.city)
    }

    private fun fetchCompanyUpdates(dateString: String, cityId: String) {
        companyRatesJob?.cancel()
        companyRatesJob = repository.getCompanyRatesForCity(dateString, cityId)
            .onEach { updates ->
                _uiState.update { it.copy(historicalRateData = updates) }
            }
            .catch { e ->
                // Silently log or handle error for dynamic island
            }
            .launchIn(viewModelScope)
    }

    private fun closeDynamicIsland() {
        companyRatesJob?.cancel()
        _uiState.update { it.copy(showDynamicIsland = false) }
    }

}
