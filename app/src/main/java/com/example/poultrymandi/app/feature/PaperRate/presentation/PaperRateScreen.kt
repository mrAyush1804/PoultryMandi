package com.example.poultrymandi.app.feature.PaperRate.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade


import com.example.poultrymandi.app.feature.PaperRate.domain.model.PaperRate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperRateScreen(
    uiState: PaperRateUiState,        // ✅ parameter se aata hai
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,             // ✅ lambda se refresh
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    // ✅ viewModel yahan nahi — sirf uiState use karo
    val isRefreshing = uiState is PaperRateUiState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,          // ✅ lambda pass karo
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is PaperRateUiState.Loading -> {
                PaperRateLoadingState()
            }
            is PaperRateUiState.Success -> {
                PaperRateList(uiState.rates)  // ✅ smart cast — no need for 'as'
            }
            is PaperRateUiState.Empty -> {
                PaperRateEmptyState()
            }
            is PaperRateUiState.Error -> {
                PaperRateErrorState(uiState.message)  // ✅ smart cast
            }
        }
    }
}

@Composable
fun PaperRateList(rates: List<PaperRate>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(rates) { rate ->
            PaperRateCard(rate)
        }
    }
}
@Composable
fun PaperRateCard(rate: PaperRate) {


    Log.d("CoilDebug", "Loading image: ${rate.imageUrl}")

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(rate.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Paper Rate Image",
                onSuccess = {
                    Log.d("CoilDebug", "✅ Image loaded successfully")
                },
                onError = {
                    Log.e("CoilDebug", "❌ Image load failed: ${it.result.throwable}")
                },
                onLoading = {
                    Log.d("CoilDebug", "⏳ Image loading...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = rate.associationName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = rate.dateLabel,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@Composable
fun PaperRateLoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun PaperRateEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No paper rates available yet.", color = Color.Gray)
    }
}

@Composable
fun PaperRateErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = Color.Red)
    }
}







// fake rate

private val fakePaperRate = PaperRate(
    id = "1",
    imageUrl = "",               // empty — placeholder dikhega
    associationName = "Pune Broilers Traders Association",
    dateLabel = "24 March 2026",
    city = "Pune",
)

private val fakeRates = listOf(fakePaperRate, fakePaperRate, fakePaperRate)

@Preview(
    showBackground = true,
    name = "Paper Rate - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PaperRateScreenDarkPreview() {
    MaterialTheme {
        PaperRateScreen(
            uiState = PaperRateUiState.Success(fakeRates),
            onBackClick = {},
            onRefresh = {},
            onNavigateToHome = {},
            onNavigateToProfile = {},
            onNavigateToNotifications = {}
        )
    }
}

