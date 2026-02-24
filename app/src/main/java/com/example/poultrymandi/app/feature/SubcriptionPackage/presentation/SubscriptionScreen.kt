package com.example.poultrymandi.app.feature.SubcriptionPackage.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.app.Core.ui.components.AppButton
import com.example.poultrymandi.app.Core.ui.components.AppToggle
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.SubcriptionPackage.domian.model.SubscriptionPlan
import com.example.poultrymandi.app.feature.SubcriptionPackage.presentation.SubscriptionViewModel

/**
 * SubscriptionScreen - Modern and clean UI for poultry market subscription.
 * Adheres to Brown Lite theme (Coffee, Beige, Cream).
 */
@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Background color: Beige
    val beigeColor = Color(0xFFFAF9F6)

    Scaffold(
        containerColor = beigeColor,
        bottomBar = {
            SubscriptionBottomBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp)
        ) {
            // Language Toggle: English, Hindi, Marathi
            item {
                AppToggle(
                    option = uiState.languages,
                    selectedOption = uiState.selectedLanguage,
                    onOptionSelected = { viewModel.onLanguageSelected(it) }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Header Section
            item {
                SubscriptionHeader()
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // 1-Week Free Trial Card
            item {
                FreeTrialCard()
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Subscription Plans List
            items(uiState.plans) { plan ->
                SubscriptionPlanCard(
                    plan = plan,
                    isSelected = plan.id == uiState.selectedPlanId,
                    onPlanSelected = { viewModel.onPlanSelected(plan.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SubscriptionHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Get Live Poultry Rates",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF3E2723), // Coffee Brown
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Daily Updates for Eggs, Broiler, and Chicken",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FreeTrialCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)), // Vibrant Green
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start Your 1-Week Free Trial!",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SubscriptionPlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onPlanSelected: () -> Unit
) {
    val creamColor = Color(0xFFFFF8E1)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) brown else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onPlanSelected() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) creamColor else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 10.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (plan.tag != null) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = brown.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = plan.tag,
                                color = brown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Text(
                        text = plan.duration,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3E2723)
                    )
                }
                Text(
                    text = plan.price,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = brown
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            plan.features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = feature,
                        color = Color.DarkGray,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SubscriptionBottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 20.dp,
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            AppButton(
                text = "Start My Free Trial",
                onClick = { /* Handle subscription logic */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubscriptionPreview() {
    SubscriptionScreen()
}
