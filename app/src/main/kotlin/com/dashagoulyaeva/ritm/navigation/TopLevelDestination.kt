package com.dashagoulyaeva.ritm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    TODAY(route = "today", label = "Сегодня", icon = Icons.Outlined.Home),
    HABITS(route = "habits", label = "Привычки", icon = Icons.Outlined.TaskAlt),
    CYCLE(route = "cycle", label = "Цикл", icon = Icons.Outlined.Loop),
    SETTINGS(route = "settings", label = "Настройки", icon = Icons.Outlined.Settings),
}
