package com.example.poultrymandi.app.Core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.theme.brown


@Composable
fun AppToggle(
    option: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val BrownColor = Color(0xFF795548) // Direct brown hex

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFF1F1F1))
            .padding(4.dp)
    ) {
        option.forEach { text ->
            val isSelected = text == selectedOption
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) Color(0xFF795548) else Color.Transparent) // Direct, no animation
                    .clickable { onOptionSelected(text) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = if (isSelected) Color.White else Color(0xFF795548),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}