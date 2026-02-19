package com.example.poultrymandi.app.feature.home.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.presentation.Components.DateSelector
import com.example.poultrymandi.app.feature.home.presentation.Components.HomeHeader


@Composable
fun HomeScreen() {

    Scaffold(
        containerColor = colorResource(id = R.color.white),
        bottomBar = {  }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(key = "header") {

                Row(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    HomeHeader()
                }

            }

            item(key = "Date") {
                Spacer(modifier = Modifier.height( 10.dp))


                val dummyDates = listOf(
                    DataItem(11, "Mon", "September", 2023, "2023-09-11", isAvailable = false),
                    DataItem(12, "Tue", "September", 2023, "2023-09-12", isAvailable = false),
                    DataItem(13, "Wed", "September", 2023, "2023-09-13", isAvailable = true),
                    DataItem(14, "Thu", "September", 2023, "2023-09-14", isAvailable = true),
                    DataItem(15, "Fri", "September", 2023, "2023-09-15", isAvailable = true),
                    DataItem(16, "Sat", "September", 2023, "2023-09-16", isAvailable = false),
                )

                var selected by remember { mutableStateOf(dummyDates[2]) }
                DateSelector(
                    dates = dummyDates,
                    selectedDate = selected,
                    onDateSelected = { selected = it },

                )
            }
        }

    }

}

@Preview(showSystemUi = true , showBackground = true)
@Composable
private fun previewHomeScreen() {

    HomeScreen()

}