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
    isVisible: Boolean,
    containerColor: Color,
    onNavigationSelected: (BottomNavScreen) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            containerColor = containerColor
        ) {
            screens.forEach { screen ->
                val isSelected = currentRoute == screen.route
                val activeColor = if (isSelected) screen.selectedColor else screen.unselectedColor

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigationSelected(screen) },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.labelMedium,
                                    color = activeColor
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Unspecified,
                        unselectedIconColor = Color.Unspecified,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}