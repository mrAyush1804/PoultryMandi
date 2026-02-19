package com.example.poultrymandi.app.feature.home.presentation.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppToggle
import com.example.poultrymandi.app.Core.ui.theme.BlackC
import org.intellij.lang.annotations.Language


@Preview
@Composable
 fun HomeHeader() {
    var selected by remember { mutableStateOf("Eng") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Market Rates",
                modifier = Modifier.padding(vertical = 2.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    color = BlackC,

                )
            )

        }

        AppToggle(
            option = listOf("Eng", "हिन्दी"),
            selectedOption = selected,
            onOptionSelected = {selected = it }
        )

    }

}