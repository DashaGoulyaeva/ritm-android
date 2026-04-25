package com.dashagoulyaeva.ritm.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashagoulyaeva.ritm.feature.cycle.presentation.cycleCalendarScreen
import com.dashagoulyaeva.ritm.feature.cycle.presentation.cycleDayJournalScreen
import com.dashagoulyaeva.ritm.feature.fasting.presentation.fastingHistoryScreen
import com.dashagoulyaeva.ritm.feature.habits.presentation.habitDetailScreen
import com.dashagoulyaeva.ritm.feature.habits.presentation.habitsScreen
import com.dashagoulyaeva.ritm.feature.settings.presentation.settingsScreen
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
                habitsScreen(onHabitClick = { id -> navController.navigate("habit_detail/$id") })
            }
            composable(
                route = "habit_detail/{habitId}",
                arguments = listOf(navArgument("habitId") { type = NavType.LongType }),
            ) {
                habitDetailScreen(
                    onBack = { navController.popBackStack() },
                    onArchived = { navController.popBackStack() },
                )
            }
            composable(TopLevelDestination.CYCLE.route) {
                cycleCalendarScreen(onDayClick = { date -> navController.navigate("cycle_journal/$date") })
            }
            composable(
                route = "cycle_journal/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) { backStack ->
                cycleDayJournalScreen(
                    date = backStack.arguments?.getString("date") ?: "",
                    onBack = { navController.popBackStack() },
                )
            }
            composable(TopLevelDestination.SETTINGS.route) {
                settingsScreen(onWaterSettingsClick = { navController.navigate("water_settings") })
            }
            composable(WATER_HISTORY_ROUTE) {
                waterHistoryScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable("fasting_history") {
                fastingHistoryScreen(onBack = { navController.popBackStack() })
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
