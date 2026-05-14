package com.ninjafarm.poultrymandi.app.feature.home.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninjafarm.poultrymandi.app.Core.navigation.Screen
import com.ninjafarm.poultrymandi.app.feature.animation.DynamicIsland
import com.ninjafarm.poultrymandi.app.feature.home.presentation.Components.*
import kotlin.math.abs
import com.ninjafarm.poultrymandi.R

/**
 * HomeScreen - Farmer Friendly Redesign.
 * Single scrollable feed with City Chips and live Company Rates.
 */
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeScreenEvent) -> Unit,
    onBottomBarVisibilityChanged: (Boolean) -> Unit = {},
    currentRoute: Screen = Screen.Home
) {
    val listState = rememberLazyListState()

    // Manage bottom bar visibility
    val isBottomBarVisible by remember(state.showDynamicIsland, listState.isScrollInProgress) {
        derivedStateOf { !state.showDynamicIsland && !listState.isScrollInProgress }
    }

    LaunchedEffect(isBottomBarVisible) {
        onBottomBarVisibilityChanged(isBottomBarVisible)
    }

    Scaffold(
        containerColor = Color.White,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {
                    HomeHeader(
                        selectedLanguage = state.selectedLanguage,
                        onLanguageSelected = { onEvent(HomeScreenEvent.LanguageSelected(it)) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 🔔 RATE UPDATE ALERT BANNER
                item(key = "rate_alert") {
                    val rates = state.historicalRateData
                    if (rates.size >= 2) {
                        val latest = rates[0]
                        val previous = rates[1]
                        if (latest.rate != previous.rate) {
                            val diff = latest.rate - previous.rate
                            val isUp = diff > 0
                            val bgColor = if (isUp) Color(0xFF1D9E75) else Color(0xFFE24B4A)
                            val arrow = if (isUp) "▲" else "▼"
                            val todayDate = remember {
                                java.text.SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    java.util.Locale.getDefault()
                                ).format(java.util.Date())
                            }

                            var visible by remember { mutableStateOf(true) }

                            LaunchedEffect(latest.rate) {
                                visible = true
                                kotlinx.coroutines.delay(5000)
                                visible = false
                            }

                            if (visible) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = bgColor)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "🔔 Rate Updated — $todayDate",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${state.selectedCityRate?.city ?: ""}  ${state.selectedCategory?.title ?: ""}  ₹${previous.rate} → ₹${latest.rate}  $arrow ₹${"%.2f".format(abs(diff))}",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. DATE SELECTOR
                item {
                    DateSelector(
                        dates = state.dates,
                        selectedDate = state.selectedDate,
                        onDateSelected = { onEvent(HomeScreenEvent.DateSelected(it)) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 3. CATEGORY SELECTOR
                item {
                    HomeCategory(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { onCategorySelected -> onEvent(HomeScreenEvent.CategorySelected(onCategorySelected)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. STATE SELECTOR
                item {
                    StateSelector(
                        states = state.states,
                        selectedState = state.selectedState,
                        onStateSelected = { onEvent(HomeScreenEvent.StateSelected(it)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ── LOADING / ERROR ───────────────
                if (state.isLoading) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorResource(id = R.color.purple_500))
                        }
                    }
                }

                state.error?.let { errorMsg ->
                    item {
                        Text(
                            text = errorMsg,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }


                state.selectedState?.let { selectedState ->
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(selectedState.cities) { city ->
                                val isSelected = city.city == state.selectedCityRate?.city
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onEvent(HomeScreenEvent.CityClicked(city)) },
                                    label = { Text(city.city) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF1D9E75),
                                        selectedLabelColor = Color.White,
                                        labelColor = Color.Black
                                    ),
                                    border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // ── REDESIGNED COMPANY RATES FOCUS ───────────────
                item(key = "company_rates") {
                    FarmerCompanyRateTable(
                        groupedRates = state.groupedCompanyRates,
                        cityName = state.selectedCityRate?.city
                            ?: state.selectedState?.cities?.firstOrNull()?.city ?: ""
                    )
                }
            }


            if (state.showDynamicIsland && state.selectedCityRate != null) {
                DynamicIsland(
                    collapsedContent = {
                        Text(
                            text = "${state.selectedCityRate?.city} Rates",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    expandedContent = { scope ->
                        DynamicIslandContent(
                            selectedCity = state.selectedCityRate?.city ?: "",
                            selectedCategory = state.selectedCategory?.title ?: "Broiler",
                            rateData = state.historicalRateData,
                            onClose = {
                                scope.close()
                                onEvent(HomeScreenEvent.DynamicIslandClosed)
                            }
                        )
                    }
                )
            }
        }
    }
}
