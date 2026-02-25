package com.example.poultrymandi.app.Core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.poultrymandi.app.Core.navigation.BottomNavScreen
import com.example.poultrymandi.app.Core.navigation.Screen

@Composable
fun PoultryBottomBar(
    screens: List<BottomNavScreen>,
    currentRoute: Screen?,
    isVisible: Boolean, // ✅ Visibility control
    containerColor: Color,
    onNavigationSelected: (BottomNavScreen) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),  // ✅ Neeche se aayega
        exit = slideOutVertically(targetOffsetY = { it })    // ✅ Neeche jaayega
    ) {
        NavigationBar(
            containerColor = containerColor
        ) {
            screens.forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigationSelected(screen) },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified // ✅ PNG original color
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Unspecified,
                        unselectedIconColor = Color.Unspecified,
                        selectedTextColor = screen.selectedColor,
                        unselectedTextColor = screen.unselectedColor,
                        indicatorColor = Color.Transparent // ✅ Gray circle nahi
                    )
                )
            }
        }
    }
}