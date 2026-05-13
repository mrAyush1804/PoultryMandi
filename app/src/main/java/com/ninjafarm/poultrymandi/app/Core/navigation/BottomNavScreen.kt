package com.ninjafarm.poultrymandi.app.Core.navigation

import androidx.annotation.DrawableRes
import com.ninjafarm.poultrymandi.R
import androidx.compose.ui.graphics.Color

sealed class BottomNavScreen(
    val route: Screen,
    val title: String,
    @DrawableRes val icon: Int,
    val selectedColor: Color = Color(0xFF2ECC71), // Success Green
    val unselectedColor: Color = Color.Gray        // Changed from Transparent to Gray
) {
    object Home : BottomNavScreen(Screen.Home, "Home", R.drawable.home__1_)
    object PaperRate : BottomNavScreen(Screen.PaperRate, "Paper Rate", R.drawable.paper)
    object Notifications : BottomNavScreen(Screen.Notifications, "Notifications", R.drawable.notification)
    object Profile : BottomNavScreen(Screen.Profile, "Profile", R.drawable.profile)
}
