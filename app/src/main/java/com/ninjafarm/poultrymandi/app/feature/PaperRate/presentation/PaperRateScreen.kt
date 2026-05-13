package com.ninjafarm.poultrymandi.app.feature.PaperRate.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ninjafarm.poultrymandi.app.feature.PaperRate.domain.model.PaperRate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperRateScreen(
    uiState: PaperRateUiState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val isRefreshing = uiState is PaperRateUiState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is PaperRateUiState.Loading -> {
                PaperRateLoadingState()
            }
            is PaperRateUiState.Success -> {
                PaperRateList(uiState.rates)
            }
            is PaperRateUiState.Empty -> {
                PaperRateEmptyState()
            }
            is PaperRateUiState.Error -> {
                PaperRateErrorState(uiState.message)
            }
        }
    }
}

@Composable
fun PaperRateList(rates: List<PaperRate>) {
    // Track which image is selected
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(rates) { rate ->
                PaperRateCard(
                    rate = rate,
                    onClick = { selectedImageUrl = rate.imageUrl }
                )
            }
        }

        // ── Fullscreen Image Overlay ──
        selectedImageUrl?.let { imageUrl ->
            FullScreenImageViewer(
                imageUrl = imageUrl,
                onClose = { selectedImageUrl = null }
            )
        }
    }
}

@Composable
fun PaperRateCard(
    rate: PaperRate,
    onClick: () -> Unit = {}
) {
    Log.d("CoilDebug", "Loading image: ${rate.imageUrl}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
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
fun FullScreenImageViewer(
    imageUrl: String,
    onClose: () -> Unit
) {
    // Intercept back press to close
    BackHandler { onClose() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { /* consume clicks */ }
    ) {
        // ── Zoomable Full Image ──
        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Full Screen Paper Rate",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        offsetX += pan.x * scale
                        offsetY += pan.y * scale
                    }
                },
            contentScale = ContentScale.Fit
        )

        // ── Close Button top right ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onClose() },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "✕",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ── Tap anywhere to close hint ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tap ✕ or pinch to zoom",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
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
    imageUrl = "",
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
