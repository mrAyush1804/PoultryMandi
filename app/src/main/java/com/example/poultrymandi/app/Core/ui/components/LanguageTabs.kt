package com.example.poultrymandi.app.Core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.Core.ui.theme.brown

/**
 * A common reusable component for language selection tabs.
 * Displays English, Hindi, and Marathi options.
 */
@Composable
fun LanguageTabs(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = listOf("English", "Hindi", "Marathi")
    
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFF1F1F1))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        languages.forEach { language ->
            val isSelected = language == selectedLanguage
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) brown else Color.Transparent)
                    .clickable { onLanguageSelected(language) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = language,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}
