package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate

@Composable
fun CompanyRateTable(rates: List<CompanyRateUpdate>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth().background(Color(0xFFFFEB3B).copy(alpha = 0.6f))
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Company", Modifier.weight(2f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Variety", Modifier.weight(1.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Rate", Modifier.weight(1.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Time", Modifier.weight(1f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        // LazyColumn with Max Height
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp)
        ) {
            items(rates.size) { index ->
                val current = rates[index]
                // Look for previous entry of SAME company in the history (next index in list as it is descending by time)
                val previous = if (index + 1 < rates.size) rates[index + 1] else null
                
                RateHistoryRow(
                    current = current,
                    previousRate = previous?.rate,
                    isEven = index % 2 == 0
                )
            }
        }
    }
}
