package com.ninjafarm.poultrymandi.app.feature.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninjafarm.poultrymandi.R
import com.ninjafarm.poultrymandi.app.feature.home.data.model.DataItem
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.*
import com.ninjafarm.poultrymandi.app.feature.home.domain.repository.RatesRepository
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

    private fun getTodayDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    private fun groupRatesByCompany(
        rates: List<CompanyRateUpdate>,
        selectedCategory: String?
    ): Map<String, List<CompanyRateUpdate>> {
        Log.d("CategoryDebug", "Filtering by category: $selectedCategory | rates size: ${rates.size}")

        // ✅ Firestore se aa rahi exact values dekho
        rates.forEach {
            Log.d("ChicksDebug",
                "company=${it.companyName} | variety='${it.variety}' | category='${it.category}'"
            )
        }

        val filtered = if (selectedCategory.isNullOrBlank()) {
            rates
        } else {
            val selected = selectedCategory.trim().lowercase()

            rates.filter { rate ->
                val variety = rate.variety.trim().lowercase()
                val category = rate.category.trim().lowercase()
                val company = rate.companyName.trim().lowercase()

                // ✅ Sirf aliases block — purana duplicate code HATAYA
                val aliases = when (selected) {
                    "chicks", "chickes", "chick" -> listOf(
                        "chicks", "chickes", "chick",
                        "day old", "doc", "dayold", "day-old"
                    )
                    "broiler" -> listOf("broiler")
                    "eggs", "egg" -> listOf("eggs", "egg")
                    else -> listOf(selected)
                }

                aliases.any { alias ->
                    variety == alias ||
                            category == alias ||
                            variety.contains(alias) ||
                            category.contains(alias) ||
                            company.contains(alias)
                }
            }
        }

        Log.d("CategoryDebug", "Total: ${rates.size} | After filter: ${filtered.size}")
        return filtered.groupBy { it.companyName }
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
                CategoryDomain("eggs", "Eggs", R.drawable.egg_tongue_face),
                CategoryDomain("chicks", "Chicks", R.drawable.boiled_chicken), // ✅ typo fix
            )

            _uiState.update {
                it.copy(
                    dates = dates,
                    selectedDate = today,
                    categories = dummyCategories,
                    selectedCategory = dummyCategories[0]
                )
            }

            today?.let {
                fetchRatesForDate(it.isAvailable1)
            }
        }
    }

    private fun fetchRatesForDate(dateString: String) {
        Log.d("HomeViewModel", "Fetching rates for: $dateString")
        ratesJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }

        ratesJob = repository.getRatesForDate(dateString)
            .onEach { states ->
                val currentState = _uiState.value
                val newSelectedState = states.find { s ->
                    s.name == currentState.selectedState?.name
                } ?: states.firstOrNull()

                val firstCity = newSelectedState?.cities?.firstOrNull()

                if (firstCity?.city != currentState.selectedCityRate?.city) {
                    firstCity?.let {
                        fetchCompanyUpdates(dateString, it.city)
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        states = states,
                        selectedState = newSelectedState,
                        selectedCityRate = firstCity
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to fetch rates"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onDateSelected(date: DataItem) {
        _uiState.update { it.copy(selectedDate = date) }
        fetchRatesForDate(date.isAvailable1)
    }

    private fun onCategorySelected(category: CategoryDomain) {
        Log.d("CategoryDebug", "Category selected: ${category.title}")
        val currentRates = _uiState.value.historicalRateData

        _uiState.update {
            it.copy(
                selectedCategory = category,
                groupedCompanyRates = groupRatesByCompany(
                    currentRates,
                    category.title
                )
            )
        }
    }

    private fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    private fun onStateSelected(state: StateDomain) {
        val firstCity = state.cities.firstOrNull()

        _uiState.update {
            it.copy(
                selectedState = state,
                selectedCityRate = firstCity
            )
        }

        if (firstCity != null) {
            val currentDate = _uiState.value.selectedDate?.isAvailable1 ?: getTodayDateString()
            fetchCompanyUpdates(currentDate, firstCity.city)
        }
    }

    private fun onCityClick(marketRate: MarketRateDomain) {
        _uiState.update {
            it.copy(selectedCityRate = marketRate)
        }
        val currentDate = _uiState.value.selectedDate?.isAvailable1 ?: getTodayDateString()
        fetchCompanyUpdates(currentDate, marketRate.city)
    }

    private fun fetchCompanyUpdates(dateString: String, cityId: String) {
        if (dateString.isBlank() || cityId.isBlank()) {
            Log.w("HomeViewModel", "Skipping company fetch — blank dateString or cityId")
            return
        }

        Log.d("HomeViewModel", "Fetching company rates: date=$dateString city=$cityId")

        companyRatesJob?.cancel()
        companyRatesJob = repository.getCompanyRatesForCity(dateString, cityId)
            .onEach { updates ->
                updates.forEach {
                    Log.d("CategoryDebug",
                        "company=${it.companyName} | variety=${it.variety} | category=${it.category} | rate=${it.rate}"
                    )
                }
                _uiState.update {
                    it.copy(
                        historicalRateData = updates,
                        groupedCompanyRates = groupRatesByCompany(
                            updates,
                            it.selectedCategory?.title
                        )
                    )
                }
            }
            .catch { e ->
                Log.e("HomeViewModel", "Company rates error: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    private fun closeDynamicIsland() {
        companyRatesJob?.cancel()
        _uiState.update { it.copy(showDynamicIsland = false) }
    }
}