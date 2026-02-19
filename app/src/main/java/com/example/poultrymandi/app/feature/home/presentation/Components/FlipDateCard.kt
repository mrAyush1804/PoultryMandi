package com.example.poultrymandi.app.feature.home.presentation.Components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.home.data.model.DataItem

@Composable
fun FlipDateCard(
    dateItem: DataItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val rotation by animateFloatAsState(
        targetValue = if (isSelected) 180f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "flipRotation"
    )

    Box(
        modifier = Modifier
            .width(58.dp)
            .height(72.dp)
            .graphicsLayer {
                rotationY = rotation        // ✅ Y axis flip
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(14.dp))
            .background(
                when {
                    isSelected          -> brown                    // Selected = brown
                    !dateItem.isAvailable -> Color(0xFFF0F0F0)     // Unavailable = light grey
                    else                -> Color.White              // Available = white
                }
            )
            .border(
                width = 1.dp,
                color = when {
                    isSelected          -> brown.copy(alpha = 0.7f)
                    !dateItem.isAvailable -> Color(0xFFE0E0E0)
                    else                -> Color(0xFFD0D0D0)
                },
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                enabled = dateItem.isAvailable  // ✅ Unavailable = click nahi hoga
            ) { onClick() }
            .alpha(if (dateItem.isAvailable) 1f else 0.4f), // ✅ Unavailable = faded
        contentAlignment = Alignment.Center
    ) {


        if (rotation <= 90f) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = dateItem.dayLabel,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${dateItem.day}",
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (dateItem.isAvailable && !isSelected) {
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(brown, CircleShape)
                    )
                }
            }
        }


        else {
            Box(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${dateItem.day}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}