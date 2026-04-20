package com.dashagoulyaeva.ritm.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dashagoulyaeva.ritm.feature.settings.presentation.waterSettingsScreen
import com.dashagoulyaeva.ritm.feature.water.presentation.waterHistoryScreen
import com.dashagoulyaeva.ritm.navigation.components.ritmBottomBar

@Composable
fun ritmNavGraph() {
    val navController = rememberNavController()
    val destinations = TopLevelDestination.entries

    Scaffold(
        bottomBar = {
            ritmBottomBar(
                navController = navController,
                destinations = destinations,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.TODAY.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(TopLevelDestination.TODAY.route) {
                todayScreen(
                    onOpenWaterHistory = { navController.navigate(WATER_HISTORY_ROUTE) },
                )
            }
            composable(TopLevelDestination.HABITS.route) {
                placeholderScreen("РџСЂРёРІС‹С‡РєРё")
            }
            composable(TopLevelDestination.CYCLE.route) {
                placeholderScreen("Р¦РёРєР»")
            }
            composable(TopLevelDestination.SETTINGS.route) {
                waterSettingsScreen()
            }
            composable(WATER_HISTORY_ROUTE) {
                waterHistoryScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}

private const val WATER_HISTORY_ROUTE = "water_history"

@Composable
private fun placeholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(title)
    }
}
