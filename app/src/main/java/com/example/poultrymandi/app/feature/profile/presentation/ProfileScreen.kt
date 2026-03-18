package com.example.poultrymandi.app.feature.profile.presentation

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.components.AppToggle
import com.example.poultrymandi.app.Core.ui.theme.BlackC
import com.example.poultrymandi.app.Core.ui.theme.brown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val bgColor = Color(0xFFF8F9FA)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackC
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header Section
            item {
                ProfileHeader(uiState.userProfile)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Language Settings
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Language Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackC,
                        modifier = Modifier.padding(bottom = 12.dp)

                    )
                    AppToggle(
                        option = uiState.languages,
                        selectedOption = uiState.selectedLanguage,
                        onOptionSelected = { viewModel.onLanguageSelected(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Menu Options with Animation
            val menuItems = listOf(
                ProfileMenuOption("Farm Details", "Manage sheds, flock count, and equipment", R.drawable.details,
                    onClick = {
                        openCustomTab(context , url = "https://poultrymandi_info.pagelet.host/privacy")
                    }),
                ProfileMenuOption("Market Preferences", "Favorite buyers, preferred trading zones",R.drawable.home),
                ProfileMenuOption("About ", "Price alerts, order status updates", R.drawable.notification),
                ProfileMenuOption("Help & Support", "Contact us, FAQs, User guide", R.drawable.outline_support_agent_24,
                    onClick = {
                        openCustomTab(context , url = "https://poultrymandi_info.pagelet.host/privacy")
                    }),
                ProfileMenuOption("About Us", "Know more about PoultryMandi", R.drawable.about_us,
                    onClick = {
                        openCustomTab(context , url = "https://poultrymandi_info.pagelet.host/privacy")
                    }),
                ProfileMenuOption("Privacy Policy", "Read our Privacy Policy", R.drawable.privatsy,
                    onClick = {
                        openCustomTab(context , url = "https://poultrymandi_info.pagelet.host/privacy")
                    }),
                ProfileMenuOption("Terms & Conditions", "Read our terms and conditions", R.drawable.privatsy,
                    onClick = {
                        openCustomTab(context, url = "https://poultrymandi_info.pagelet.host/privacy")
                    }),
                ProfileMenuOption("Setting", "Read our Setting", R.drawable.outline_settings_heart_24),
                ProfileMenuOption("How to Use PoultryMandi", "Read our How to Use PoultryMandi", R.drawable.howtouse)


            )

            itemsIndexed(menuItems) { index, item ->
                AnimatedProfileOption(item, index)
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(32.dp))
                LogoutButton(onClick = { viewModel.logout() })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(profile: com.example.poultrymandi.app.feature.profile.domain.model.UserProfile?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = painterResource(id = R.drawable.chicks), // Replace with actual profile image logic
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { /* Edit image */ },
                color = Color(0xFF2ECC71),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = profile?.name ?: "User Name",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = BlackC
        )
        Text(
            text = profile?.role ?: "Role",
            fontSize = 14.sp,
            color = Color(0xFF2ECC71),
            fontWeight = FontWeight.SemiBold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = profile?.location ?: "Location",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AnimatedProfileOption(item: ProfileMenuOption, index: Int) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        ) + slideInVertically(
            initialOffsetY = { it * (index + 1) / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        ProfileOptionCard(item)
    }
}

@Composable
fun ProfileOptionCard(item: ProfileMenuOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {  item.onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1FDF5)
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    tint = Color(0xFF2ECC71),
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackC
                )
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )


            }
            Icon(
                Icons.Default.Build,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFFFEBEE)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFFFFFBFA)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = Color(0xFFEF5350)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Logout",
                color = Color(0xFFEF5350),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

data class ProfileMenuOption(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val  onClick : () -> Unit={}
)


fun openCustomTab(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    intent.launchUrl(context, Uri.parse(url))
}

/*@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}*/
