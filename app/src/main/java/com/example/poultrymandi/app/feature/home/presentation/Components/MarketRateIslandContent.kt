package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.feature.home.domain.data.MarketRateDomain

@Composable
fun MarketRateIslandContent(
    rate: MarketRateDomain,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                color = Color.White
            )
            Text(
                text = "Close",
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Table Header
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Day", Modifier.weight(1f), color = Color.Gray, fontSize = 12.sp)
            Text("Price", Modifier.weight(1f), color = Color.Gray, fontSize = 12.sp)
            Text("Trend", Modifier.weight(1f), color = Color.Gray, fontSize = 12.sp)
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
                    color = if (diff >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
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
private fun IslandRateRow(label: String, price: Double, isToday: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, Modifier.weight(1f), color = if (isToday) Color.White else Color.White.copy(alpha = 0.6f))
        Text("₹ ${"%.2f".format(price)}", Modifier.weight(1f), color = Color.White, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
        Box(Modifier.weight(1f)) {
            if (isToday) {
                Text("LIVE", color = Color(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
