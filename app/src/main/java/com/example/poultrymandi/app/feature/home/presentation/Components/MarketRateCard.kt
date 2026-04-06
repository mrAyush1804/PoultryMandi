package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import com.example.poultrymandi.app.feature.home.domain.data.StateDomain
import kotlin.math.abs

@Composable
fun MarketRateCard(
    stateDomain: StateDomain,
    selectedCategory: String = "Broiler",
    onCityClick: (MarketRateDomain) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "chevron"
    )

    val firstCity = stateDomain.cities.firstOrNull()
    val displayPrice = firstCity?.getPriceForCategory(selectedCategory) ?: 0.0
    val headerDiff = firstCity?.getPriceChangeForCategory(selectedCategory) ?: 0.0
    
    val headerTrendColor = when {
        headerDiff > 0 -> Color(0xFF1D9E75)
        headerDiff < 0 -> Color(0xFFE24B4A)
        else -> Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // --- Header Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stateDomain.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "${stateDomain.cities.size} cities • $selectedCategory",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${"%.2f".format(displayPrice)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = headerTrendColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation),
                        tint = Color.Gray
                    )
                }
            }

            // --- Expanded Section ---
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    stateDomain.cities.forEachIndexed { index, city ->
                        CityRateRow(
                            city = city,
                            selectedCategory = selectedCategory,
                            onClick = { onCityClick(city) }
                        )
                        if (index < stateDomain.cities.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityRateRow(
    city: MarketRateDomain,
    selectedCategory: String,
    onClick: () -> Unit
) {
    val price = city.getPriceForCategory(selectedCategory)
    val diff = city.getPriceChangeForCategory(selectedCategory)

    val trendColor = when {
        diff > 0 -> Color(0xFF1D9E75)
        diff < 0 -> Color(0xFFE24B4A)
        else -> Color.Gray
    }

    val trendText = when {
        diff > 0 -> "▲ +₹${"%.2f".format(diff)}"
        diff < 0 -> "▼ -₹${"%.2f".format(abs(diff))}"
        else -> "— Stable"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = city.city,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "₹${"%.2f".format(price)}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = trendText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = trendColor,
                modifier = Modifier.width(85.dp)
            )
        }
    }
}
