package com.example.poultrymandi.app.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.R
import com.example.poultrymandi.app.feature.animation.DynamicIsland
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import com.example.poultrymandi.app.feature.home.presentation.Components.*


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = colorResource(id = R.color.white),
        bottomBar = { }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                item(key = "header") {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        HomeHeader(
                            selectedLanguage = uiState.selectedLanguage,
                            onLanguageSelected = { viewModel.onLanguageSelected(it) },
                        )
                    }
                }

                item(key = "Date") {
                    Spacer(modifier = Modifier.height(10.dp))

                    DateSelector(
                        dates = uiState.dates,
                        selectedDate = uiState.selectedDate,
                        onDateSelected = { viewModel.onDateSelected(it) },
                    )
                }

                item(key = "category") {
                    Spacer(modifier = Modifier.height(24.dp))

                    HomeCategory(
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { viewModel.onCategorySelected(it) }
                    )
                }

                item(key = "state_selector") {
                    Spacer(modifier = Modifier.height(24.dp))
                    StateSelector(
                        states = uiState.states,
                        selectedState = uiState.selectedState,
                        onStateSelected = { viewModel.onStateSelected(it) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(
                    items = uiState.selectedState?.cities ?: emptyList(),
                    key = { it.city }
                ) { marketRate ->
                    MarketRateCard(
                        marketRate = marketRate,

                        onCityClick = { viewModel.onCityClick(marketRate) }
                    )
                }
            }

            if (uiState.showDynamicIsland && uiState.selectedCityRate != null) {
                DynamicIsland(
                    collapsedContent = {
                        Text(
                            text = "${uiState.selectedCityRate?.city} Rates",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    expandedContent = { scope ->
                        DynamicIslandContent(
                            rate = uiState.selectedCityRate!!,
                            onClose = {
                                scope.close()
                                viewModel.closeDynamicIsland()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DynamicIslandContent(
    rate: MarketRateDomain,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${rate.city} Market",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Close",
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Day", Modifier.weight(1f), color = Color.Black, fontSize = 12.sp)
            Text("Price", Modifier.weight(1f), color = Color.Black, fontSize = 12.sp)
            Text("Trend", Modifier.weight(1f), color = Color.Black, fontSize = 12.sp)
        }
        
        HorizontalDivider(Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f))

        IslandRateRow("Yesterday", rate.yesterdayPrice)
        IslandRateRow("Today", rate.todayPrice, isToday = true)
        IslandRateRow("Tomorrow", rate.tomorrowPrice ?: 0.0)
        IslandRateRow("Day After", rate.dayAfterTomorrowPrice ?: 0.0)

        Spacer(modifier = Modifier.height(24.dp))

        val diff = rate.priceChange
        val avg = (rate.yesterdayPrice + rate.todayPrice + (rate.tomorrowPrice ?: rate.todayPrice)) / 3.0

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Today's Change", color = Color.White.copy(alpha = 0.7f))
                Text(
                    text = "${if (diff >= 0) "+" else ""}${ "%.2f".format(diff)}",
                    color = if (diff >= 0) Color(0xFF03A9F4) else Color(0xFFCDDC39),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("3-Day Average", color = Color.White.copy(alpha = 0.7f))
                Text(
                    text = "₹ ${"%.2f".format(avg)}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun IslandRateRow(label: String, price: Double, isToday: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, Modifier.weight(1f), color = if (isToday) Color.Black else Color.Black.copy(alpha = 0.6f))
        Text("₹ ${"%.2f".format(price)}", Modifier.weight(1f), color = Color.White, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
        Box(Modifier.weight(1f)) {
            if (isToday) {
                Text("LIVE", color = Color(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun previewHomeScreen() {
    HomeScreen()
}
