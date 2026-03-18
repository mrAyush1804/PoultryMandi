package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.Core.ui.theme.oceanBule
import com.example.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate
import com.example.poultrymandi.app.feature.home.domain.data.getMockRateData

/**
 * Refactored DynamicIslandContent - Main container for company-wise historical rates.
 * Follows strict styling rules: Background #1A1110 and ALL text Color.Black.
 */
@Composable
fun DynamicIslandContent(
    selectedCity: String,
    selectedCategory: String,
    rateData: List<CompanyRateUpdate>,
    onClose: () -> Unit
) {
    // Filter data by city and category (Case insensitive for safety)
    val filteredData = rateData.filter {
        it.city.equals(selectedCity, ignoreCase = true) &&
                it.category.equals(selectedCategory, ignoreCase = true)
    }

    // Extract unique companies for the selector
    val companies = filteredData.map { it.companyName }.distinct()

    // Local state for selected company, defaults to first available
    var selectedCompany by remember(companies) {
        mutableStateOf(companies.firstOrNull() ?: "")
    }

    val bubbleGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),  // Top — Lightest Aqua Foam
            Color(0xFF80DEEA),  // Mid — Soft Cyan Water
            Color(0xFF00BCD4),  // Deep — Pure Bubble Blue
            Color(0xFF006064).copy(alpha = 0.6f) // Bottom — Deep Ocean Shadow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bubbleGradient)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize() // Deep dark brown-black
                .padding(16.dp)
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$selectedCity Market",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Mandatory requirement
                )
                Text(
                    text = "Close",
                    modifier = Modifier.clickable { onClose() },
                    color = Color.Black, // Mandatory requirement
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Company Selector (Horizontal Chips)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(companies) { company ->
                    val isSelected = company == selectedCompany
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { selectedCompany = company },
                        color = if (isSelected) oceanBule else Color.Black.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = company,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else Color.Black, // Selected chip uses white for contrast
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Get rates for selected company
            val companyRates = filteredData.filter { it.companyName == selectedCompany }

            if (companyRates.isNotEmpty()) {
                // Summary Header Card
                SummaryHeaderCard(companyRates)

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Rate Table
                CompanyRateTable(companyRates)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No data available", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun SummaryHeaderCard(rates: List<CompanyRateUpdate>) {
    // Calculations for the summary
    val openingRate = rates.last().rate // Assuming chronological order, last is opening
    val latestRate = rates.first().rate
    val highRate = rates.maxOf { it.rate }
    val lowRate = rates.minOf { it.rate }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Today, 04 Jun",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Updates: ${rates.size}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryStatItem("Open", openingRate)
                SummaryStatItem("Latest", latestRate)
                SummaryStatItem("High", highRate)
                SummaryStatItem("Low", lowRate)
            }
        }
    }
}

@Composable
private fun SummaryStatItem(label: String, value: Double) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = oceanBule.copy(alpha = 0.6f)
        )
        Text(
            text = "₹$value",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DynamicIslandContentPreview() {
    DynamicIslandContent(
        selectedCity = "Indore",
        selectedCategory = "Broiler",
        rateData = getMockRateData(),
        onClose = {}
    )
}

