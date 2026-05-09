package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FarmerCompanyRateTable(
    groupedRates: Map<String, List<CompanyRateUpdate>>,
    cityName: String,
    modifier: Modifier = Modifier
) {

    val expandedCompanies = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
      Row(
          modifier = modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
      ) {

          Text(
              text = "Company Rates :",
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = Color.Black,

          )

          Text(
              text = ": $cityName",
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = Color.Red,
              modifier = Modifier.padding(horizontal = 10.dp)
          )
      }


        if (groupedRates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No rates available", fontSize = 16.sp, color = Color.Gray)
            }
            return@Column
        }

        // ── Table Header ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFFFEB3B),
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Company",
                modifier = Modifier.weight(3f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = "Rate",
                modifier = Modifier.weight(2f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = "Chng",
                modifier = Modifier.weight(1.2f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Time",
                modifier = Modifier.weight(2f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }

        // ── One row per company ──
        groupedRates.entries.forEachIndexed { index, (companyName, history) ->
            val bgColor = if (index % 2 == 0) Color(0xFFE0F7FA) else Color(0xFFB2EBF2)
            val isExpanded = expandedCompanies[companyName] ?: false

            val latest = history.first()           // most recent rate
            val previous = if (history.size > 1) history[1] else null
            val diff = if (previous != null) latest.rate - previous.rate else 0.0
            val changeCount = history.size         // total updates today

            val trendColor = when {
                diff > 0 -> Color(0xFF1D9E75)
                diff < 0 -> Color(0xFFE24B4A)
                else -> Color.Gray
            }
            val trendArrow = when {
                diff > 0 -> "▲"
                diff < 0 -> "▼"
                else -> "—"
            }

            // ── Main Company Row ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Column 1: Company Name + Variety + Previous Rate
                    Column(modifier = Modifier.weight(3f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (latest.isLive) {
                                LivePulsingDot()
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = companyName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 18.sp
                            )
                        }
                        if (previous != null) {
                            Text(
                                text = "was ₹${previous.rate.toInt()}",
                                fontSize = 11.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Text(
                            text = latest.variety,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    // Column 2: Rate + Arrow
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "₹${latest.rate.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                        Text(
                            text = trendArrow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = trendColor,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Column 3: Change Count
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${changeCount}x",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFE65100),
                            maxLines = 1
                        )
                        Text(
                            text = "today",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )
                    }

                    // Column 4: Time + Date + History toggle
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = latest.timestamp,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            maxLines = 1
                        )
                        Text(
                            text = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date()),
                            fontSize = 10.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )
                        if (history.size > 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isExpanded) "▲ Hide" else "▼ History",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0),
                                maxLines = 1,
                                modifier = Modifier.clickable {
                                    expandedCompanies[companyName] = !isExpanded
                                }
                            )
                        }
                    }
                }

                // ── Expanded History Section ──
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F8E9))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // History sub-header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text("Time",     Modifier.weight(1.5f), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Rate",     Modifier.weight(1.5f), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Change",   Modifier.weight(2f),   fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                        // Each historical entry
                        history.forEachIndexed { hIndex, entry ->
                            val prevEntry = if (hIndex + 1 < history.size) history[hIndex + 1] else null
                            val entryDiff = if (prevEntry != null) entry.rate - prevEntry.rate else 0.0
                            val entryColor = when {
                                entryDiff > 0 -> Color(0xFF1D9E75)
                                entryDiff < 0 -> Color(0xFFE24B4A)
                                else -> Color.Gray
                            }
                            val entryArrow = when {
                                entryDiff > 0 -> "▲ +₹${entryDiff.toInt()}"
                                entryDiff < 0 -> "▼ -₹${Math.abs(entryDiff).toInt()}"
                                else -> "— No change"
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = entry.timestamp,
                                    modifier = Modifier.weight(1.5f),
                                    fontSize = 13.sp,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                                Text(
                                    text = "₹${entry.rate.toInt()}",
                                    modifier = Modifier.weight(1.5f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace,
                                    maxLines = 1
                                )
                                Text(
                                    text = entryArrow,
                                    modifier = Modifier.weight(2f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = entryColor,
                                    maxLines = 1
                                )
                            }
                            if (hIndex < history.size - 1) {
                                HorizontalDivider(color = Color.Gray.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }

            if (index < groupedRates.size - 1) {
                HorizontalDivider(color = Color.White, thickness = 2.dp)
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
