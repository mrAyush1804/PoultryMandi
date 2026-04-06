package com.example.poultrymandi.app.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.navigation.Screen
import com.example.poultrymandi.app.feature.animation.DynamicIsland
import com.example.poultrymandi.app.feature.home.presentation.Components.*

/**
 * HomeScreen - The main hub for market rates.
 * Real-time Firestore integration with loading and error states.
 */
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeScreenEvent) -> Unit,
    onBottomBarVisibilityChanged: (Boolean) -> Unit = {},
    currentRoute: Screen = Screen.Home
) {
    val listState = rememberLazyListState()

    // Manage bottom bar visibility based on scroll and Dynamic Island state
    val isBottomBarVisible by remember(state.showDynamicIsland, listState.isScrollInProgress) {
        derivedStateOf {
            !state.showDynamicIsland && !listState.isScrollInProgress
        }
    }

    LaunchedEffect(isBottomBarVisible) {
        onBottomBarVisibilityChanged(isBottomBarVisible)
    }

    Scaffold(
        containerColor = colorResource(id = R.color.white),
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Header Section
                item(key = "header") {
                    Row(modifier = Modifier.padding(horizontal = 5.dp)) {
                        HomeHeader(
                            selectedLanguage = state.selectedLanguage,
                            onLanguageSelected = { onEvent(HomeScreenEvent.LanguageSelected(it)) },
                        )
                    }
                }

                // Date Selection Section
                item(key = "Date") {
                    Spacer(modifier = Modifier.height(10.dp))
                    DateSelector(
                        dates = state.dates,
                        selectedDate = state.selectedDate,
                        onDateSelected = { onEvent(HomeScreenEvent.DateSelected(it)) },
                    )
                }

                // Category Selection Section (Broiler, Eggs, Chick)
                item(key = "category") {
                    Spacer(modifier = Modifier.height(24.dp))
                    HomeCategory(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { onEvent(HomeScreenEvent.CategorySelected(it)) }
                    )
                }

                // State/Location Selector
                item(key = "state_selector") {
                    Spacer(modifier = Modifier.height(24.dp))
                    StateSelector(
                        states = state.states,
                        selectedState = state.selectedState,
                        onStateSelected = { onEvent(HomeScreenEvent.StateSelected(it)) }
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Loading State
                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colorResource(id = R.color.purple_500))
                        }
                    }
                }

                // Error State
                state.error?.let { errorMsg ->
                    item {
                        Text(
                            text = errorMsg,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }


                items(
                    items = state.states,        // ← states list
                    key = { it.name }            // ← state name as key
                ) { stateDomain ->
                    MarketRateCard(
                        stateDomain = stateDomain,  // ← NEW parameter
                        selectedCategory = state.selectedCategory?.title ?: "Broiler",
                        onCityClick = { cityRate ->
                            onEvent(HomeScreenEvent.CityClicked(cityRate))

                        }
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
