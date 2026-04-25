package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.feature.home.presentation.StepsUiState
import com.dashagoulyaeva.ritm.feature.home.presentation.StepsViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import com.dashagoulyaeva.ritm.feature.fasting.presentation.fastingBottomSheet
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.water.presentation.WaterViewModel
import com.dashagoulyaeva.ritm.feature.water.presentation.waterQuickLogBottomSheet
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun todayScreen(
    onOpenWaterHistory: () -> Unit = {},
    onStepsHistoryClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    waterViewModel: WaterViewModel = hiltViewModel(),
    stepsViewModel: StepsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val waterState by waterViewModel.uiState.collectAsState()
    val stepsState by stepsViewModel.uiState.collectAsState()
    val today = state.todayState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Сегодня") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showWaterSheet() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Быстрый лог воды",
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // T-05: WaterWidget
            item {
                waterWidget(
                    waterCount = waterState.todayCount,
                    waterGoal = waterState.dailyGoal,
                    onAddClick = { viewModel.showWaterSheet() },
                    onHistoryClick = onOpenWaterHistory,
                )
            }

            // T-07: HabitsWidget
            item {
                habitsWidget(
                    habits = today.habits,
                    checkedIds = today.habitCheckedIds,
                    onToggle = { habitId ->
                        val isCurrentlyChecked = today.habitCheckedIds.contains(habitId)
                        viewModel.toggleHabit(habitId, !isCurrentlyChecked)
                    },
                )
            }

            // T-06: FastingWidget
            item {
                fastingWidget(
                    remainingMs = today.fastingRemainingMs,
                    isActive = today.activeFastingSession != null,
                    onStartClick = { viewModel.showFastingSheet() },
                )
            }

            // T-04: CycleWidget
            item {
                cycleWidget(cycleDayInfo = today.cycleDayInfo)
            }

            // T-08: StepsWidget
            item {
                stepsWidget(
                    state = stepsState,
                    onHistoryClick = onStepsHistoryClick,
                )
            }
        }

        if (state.showWaterSheet) {
            waterQuickLogBottomSheet(
                uiState = waterState,
                onDismissRequest = { viewModel.hideWaterSheet() },
                onAddEntry = { type ->
                    waterViewModel.addEntry(type)
                    viewModel.hideWaterSheet()
                },
            )
        }

        if (state.showFastingSheet) {
            fastingBottomSheet(
                onDismiss = { viewModel.hideFastingSheet() },
            )
        }
    }
}

// ── T-05: Water ───────────────────────────────────────────────────────────────

@Composable
private fun waterWidget(
    waterCount: Int,
    waterGoal: Int,
    onAddClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Text(
                text = "Вода",
                style = MaterialTheme.typography.titleMedium,
            )
            if (waterGoal == 0) {
                Text(
                    text = "Цель не задана",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "Сегодня: $waterCount / $waterGoal стаканов",
                    style = MaterialTheme.typography.bodyMedium,
                )
                val progress = waterCount.toFloat() / waterGoal
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ritmButton(
                    text = "Добавить",
                    onClick = onAddClick,
                )
                ritmButton(
                    text = "История",
                    onClick = onHistoryClick,
                )
            }
        }
    }
}

// ── T-07: Habits ──────────────────────────────────────────────────────────────

@Composable
private fun habitsWidget(
    habits: List<Habit>,
    checkedIds: Set<Long>,
    onToggle: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
            Text(
                text = "Привычки",
                style = MaterialTheme.typography.titleMedium,
            )
            if (habits.isEmpty()) {
                Text(
                    text = "Нет привычек на сегодня",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                habits.take(3).forEach { habit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                    ) {
                        Checkbox(
                            checked = checkedIds.contains(habit.id),
                            onCheckedChange = { onToggle(habit.id) },
                        )
                        Text(
                            text = "${habit.iconEmoji} ${habit.title}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

// ── T-06: Fasting ─────────────────────────────────────────────────────────────

@Composable
private fun fastingWidget(
    remainingMs: Long?,
    isActive: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Text(
                text = "Голодание",
                style = MaterialTheme.typography.titleMedium,
            )
            if (isActive && remainingMs != null) {
                val hours = TimeUnit.MILLISECONDS.toHours(remainingMs)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMs) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMs) % 60
                Text(
                    text = "Осталось: %02d:%02d:%02d".format(hours, minutes, seconds),
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else if (isActive) {
                Text(
                    text = "Сессия активна",
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                ritmButton(
                    text = "Начать голодание",
                    onClick = onStartClick,
                )
            }
        }
    }
}

// ── T-04: Cycle ───────────────────────────────────────────────────────────────

@Composable
private fun cycleWidget(
    cycleDayInfo: CycleDayInfo?,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Text(
                text = "Цикл",
                style = MaterialTheme.typography.titleMedium,
            )
            if (cycleDayInfo == null || cycleDayInfo.dayOfCycle == 0) {
                Text(
                    text = "Цикл не настроен",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "День цикла: ${cycleDayInfo.dayOfCycle}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Фаза: ${cycleDayInfo.phase.displayName()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun CyclePhase.displayName(): String = when (this) {
    CyclePhase.MENSTRUAL -> "Менструальная"
    CyclePhase.FOLLICULAR -> "Фолликулярная"
    CyclePhase.OVULATION -> "Овуляция"
    CyclePhase.LUTEAL -> "Лютеиновая"
    CyclePhase.UNKNOWN -> "Неизвестно"
}

// ── T-08: Steps ───────────────────────────────────────────────────────────────

@Composable
private fun stepsWidget(
    state: StepsUiState,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            Text(
                text = "Шаги",
                style = MaterialTheme.typography.titleMedium,
            )
            when {
                !state.isAvailable -> {
                    Text(
                        text = "Шагомер недоступен на этом устройстве",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    Text(
                        text = "${state.todaySteps} / ${state.dailyGoal}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    val progress = if (state.dailyGoal > 0) {
                        state.todaySteps.toFloat() / state.dailyGoal
                    } else {
                        0f
                    }
                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    TextButton(onClick = onHistoryClick) {
                        Text("История")
                    }
                }
            }
        }
    }
}
