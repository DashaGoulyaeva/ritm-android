package com.dashagoulyaeva.ritm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    TODAY(route = "today", label = "день", icon = Icons.Outlined.Home),
    CYCLE(route = "cycle", label = "цикл", icon = Icons.Outlined.Loop),
    WATER(route = "water", label = "вода", icon = Icons.Outlined.Opacity),
    HABITS(route = "habits", label = "привычки", icon = Icons.Outlined.TaskAlt),
    MORE(route = "more", label = "ещё", icon = Icons.Outlined.Menu),
}
