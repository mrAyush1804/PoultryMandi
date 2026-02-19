package com.example.poultrymandi.app.feature.home.presentation.Components


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.home.data.model.DataItem

@Composable
fun DateSelector(
    dates: List<DataItem>,
    selectedDate: DataItem?,
    onDateSelected: (DataItem) -> Unit,
    modifier: Modifier = Modifier,
    monthYear: String = "September 2023"  // ← Dynamic baad me hoga
) {
    val listState = rememberLazyListState()


    LaunchedEffect(selectedDate) {
        selectedDate?.let { selected ->
            val index = dates.indexOfFirst { it.day == selected.day }
            if (index >= 0) listState.animateScrollToItem(index)
        }
    }

    Column(modifier = modifier) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = monthYear,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )


            AnimatedContent(
                targetState = selectedDate,
                label = "selectedBadge"
            ) { date ->
                if (date != null) {
                    Text(
                        text = "${date.dayLabel}, ${date.day}",
                        color = brown,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                } else {
                    // Nothing — placeholder space
                    Spacer(Modifier.width(80.dp))
                }
            }
        }

        Spacer(Modifier.height(10.dp))


        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(
                items = dates,
                key = { it.day }
            ) { dateItem ->
                FlipDateCard(
                    dateItem = dateItem,
                    isSelected = dateItem.day == selectedDate?.day,
                    onClick = { onDateSelected(dateItem) }
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun DateSelectorPreview() {
    val dummyDates = listOf(
        DataItem(11, "Mon", "September", 2023, "2023-09-11", isAvailable = false),
        DataItem(12, "Tue", "September", 2023, "2023-09-12", isAvailable = false),
        DataItem(13, "Wed", "September", 2023, "2023-09-13", isAvailable = true),
        DataItem(14, "Thu", "September", 2023, "2023-09-14", isAvailable = true),
        DataItem(15, "Fri", "September", 2023, "2023-09-15", isAvailable = true),
        DataItem(16, "Sat", "September", 2023, "2023-09-16", isAvailable = false),
    )

    var selected by remember { mutableStateOf(dummyDates[2]) }

    Column(modifier = Modifier.padding(16.dp)) {
        DateSelector(
            dates = dummyDates,
            selectedDate = selected,
            onDateSelected = { selected = it },
            monthYear = "September 2023"
        )
    }
}