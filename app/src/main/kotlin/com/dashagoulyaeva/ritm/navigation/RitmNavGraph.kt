package com.dashagoulyaeva.ritm.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.dashagoulyaeva.ritm.feature.home.presentation.stepsHistoryScreen
import com.dashagoulyaeva.ritm.feature.home.presentation.todayScreen
import com.dashagoulyaeva.ritm.feature.settings.presentation.fastingSettingsScreen
import com.dashagoulyaeva.ritm.feature.settings.presentation.habitsSettingsScreen
import com.dashagoulyaeva.ritm.feature.settings.presentation.onboardingScreen
import com.dashagoulyaeva.ritm.feature.settings.presentation.reminderSettingsScreen
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
            todayRoutes(navController)
            habitRoutes(navController)
            cycleRoutes(navController)
            settingsRoutes(navController)
            historyRoutes(navController)
        }
    }
}

private const val WATER_HISTORY_ROUTE = "water_history"
private const val STEPS_HISTORY_ROUTE = "steps_history"
private const val FASTING_HISTORY_ROUTE = "fasting_history"

private fun NavGraphBuilder.todayRoutes(navController: NavHostController) {
    composable(TopLevelDestination.TODAY.route) {
        todayScreen(
            onOpenWaterHistory = { navController.navigate(WATER_HISTORY_ROUTE) },
            onStepsHistoryClick = { navController.navigate(STEPS_HISTORY_ROUTE) },
        )
    }
}

private fun NavGraphBuilder.habitRoutes(navController: NavHostController) {
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
}

private fun NavGraphBuilder.cycleRoutes(navController: NavHostController) {
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
}

private fun NavGraphBuilder.settingsRoutes(navController: NavHostController) {
    composable(TopLevelDestination.SETTINGS.route) {
        settingsScreen(
            onWaterSettingsClick = { navController.navigate("water_settings") },
            onFastingSettingsClick = { navController.navigate("fasting_settings") },
            onHabitsSettingsClick = { navController.navigate("habits_settings") },
            onReminderSettingsClick = { navController.navigate("reminder_settings") },
        )
    }
    composable("water_settings") {
        waterSettingsScreen(onBack = { navController.popBackStack() })
    }
    composable("fasting_settings") {
        fastingSettingsScreen(onBack = { navController.popBackStack() })
    }
    composable("habits_settings") {
        habitsSettingsScreen(onBack = { navController.popBackStack() })
    }
    composable("reminder_settings") {
        reminderSettingsScreen(onBack = { navController.popBackStack() })
    }
    composable("onboarding") {
        onboardingScreen(
            onFinish = {
                navController.navigate(TopLevelDestination.TODAY.route) {
                    popUpTo("onboarding") { inclusive = true }
                }
            },
        )
    }
}

private fun NavGraphBuilder.historyRoutes(navController: NavHostController) {
    composable(WATER_HISTORY_ROUTE) {
        waterHistoryScreen(onBackClick = { navController.popBackStack() })
    }
    composable(FASTING_HISTORY_ROUTE) {
        fastingHistoryScreen(onBack = { navController.popBackStack() })
    }
    composable(STEPS_HISTORY_ROUTE) {
        stepsHistoryScreen(onBack = { navController.popBackStack() })
    }
}
