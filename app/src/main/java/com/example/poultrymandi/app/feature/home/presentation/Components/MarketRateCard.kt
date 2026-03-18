package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain
import kotlin.math.abs
import com.example.poultrymandi.R

/**
 * Enhanced MarketRateCard - Displays city market rates with category-specific data.
 */
@Composable
fun MarketRateCard(
    marketRate: MarketRateDomain,
    selectedCategory: String = "Broiler",
    onCityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Use category-specific price logic
    val displayPrice = marketRate.getPriceForCategory(selectedCategory)
    val displayYesterday = marketRate.getYesterdayPriceForCategory(selectedCategory)
    val diff = marketRate.getPriceChangeForCategory(selectedCategory)

    val trendColor = when {
        diff > 0 -> Color(0xFF4CAF50) // Green for increase
        diff < 0 -> Color(0xFFF44336) // Red for decrease
        else -> Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCityClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // City and Today's Main Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = marketRate.city,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Text(
                        text = "Last Updated: Just now",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹ ${"%.2f".format(displayPrice)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = trendColor
                    )
                    Text(
                        text = marketRate.unit,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Rate Comparison Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RateStatItem(label = "Yesterday", value = displayYesterday)
                
                // Past updated rate - Placeholder logic using diff
                RateStatItem(label = "Change", value = abs(diff))
                
                ComparisonBadge(diff = diff)
            }
        }
    }
}

@Composable
private fun RateStatItem(label: String, value: Double) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "₹ ${"%.2f".format(value)}",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun ComparisonBadge(diff: Double) {
    val (color, icon, text) = when {
        diff > 0 -> Triple(
            Color(0xFFE8F5E9),
             R.drawable.trending_up,
            "+₹${"%.2f".format(abs(diff))} Up"
        )
        diff < 0 -> Triple(
            Color(0xFFFFEBEE),
             R.drawable.trending_down,
            "-₹${"%.2f".format(abs(diff))} Down"
        )
        else -> Triple(
            Color(0xFFF5F5F5),
             R.drawable.trending_flat,

            "Stable"
        )
    }

    val contentColor = when {
        diff > 0 -> Color(0xFF2E7D32)
        diff < 0 -> Color(0xFFC62828)
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(14.dp),

        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
