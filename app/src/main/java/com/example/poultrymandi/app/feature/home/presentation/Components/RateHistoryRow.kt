package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.Core.ui.theme.darkBlue
import com.example.poultrymandi.app.Core.ui.theme.oceanBule
import com.example.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate

@Composable
fun RateHistoryRow(
    current: CompanyRateUpdate,
    previousRate: Double?,
    isEven: Boolean
) {
    val backgroundColor = if (isEven) oceanBule else darkBlue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Company Name
        Text(
            text = current.companyName,
            modifier = Modifier.weight(2f),
            color = Color.Black, // Mandatory requirement: ALL text Color.Black
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 2. Variety
        Text(
            text = current.variety,
            modifier = Modifier.weight(1.5f),
            color = Color.Black, // Mandatory requirement: ALL text Color.Black
            fontSize = 12.sp
        )

        // 3. Rate + Trend + Live Dot
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (current.isLive) {
                LivePulsingDot()
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = "₹${current.rate}",
                color = Color.Black, // Mandatory requirement: ALL text Color.Black
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Direction Indicator
            val (trendIcon, trendColor) = when {
                previousRate == null -> "" to Color.Gray
                current.rate > previousRate -> "▲" to Color.Green
                current.rate < previousRate -> "▼" to Color.Red
                else -> "—" to Color.Gray
            }
            
            Text(text = trendIcon, color = trendColor, fontSize = 10.sp)
        }

        // 4. Time
        Text(
            text = current.timestamp,
            modifier = Modifier.weight(1f),
            color = Color.Black, // Mandatory requirement: ALL text Color.Black
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LivePulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(6.dp)
            .alpha(alpha)
            .background(Color.Green, CircleShape)
    )
}

@Preview
@Composable
fun RateHistoryRowPreview() {
    Column {
        RateHistoryRow(
            current = CompanyRateUpdate("1", "Indore", "IB Group", "Broiler", "Standard", 102.0, "5m ago", true),
            previousRate = 100.0,
            isEven = true
        )
        RateHistoryRow(
            current = CompanyRateUpdate("2", "Indore", "IB Group", "Broiler", "Standard", 98.0, "2h ago"),
            previousRate = 102.0,
            isEven = false
        )
    }
}
