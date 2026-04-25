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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.RitmRhythm
import com.dashagoulyaeva.ritm.core.ui.components.rhythmOrb
import com.dashagoulyaeva.ritm.core.ui.components.ritmBanner
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.WaterAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import com.dashagoulyaeva.ritm.feature.fasting.presentation.fastingBottomSheet
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.water.presentation.WaterViewModel
import com.dashagoulyaeva.ritm.feature.water.presentation.waterQuickLogBottomSheet
import java.util.concurrent.TimeUnit

private const val MAX_TODAY_HABITS = 3
private const val MINUTES_PER_HOUR = 60
private const val SECONDS_PER_MINUTE = 60

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                todayHero(
                    waterCount = waterState.todayCount,
                    waterGoal = waterState.dailyGoal,
                    stepsState = stepsState,
                    isFastingActive = today.activeFastingSession != null,
                    habitsDone = today.habitCheckedIds.size,
                    habitsTotal = today.habits.size,
                    cycleDayInfo = today.cycleDayInfo,
                )
            }

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
                stepsTodayWidget(
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

@Suppress("LongParameterList")
@Composable
private fun todayHero(
    waterCount: Int,
    waterGoal: Int,
    stepsState: StepsUiState,
    isFastingActive: Boolean,
    habitsDone: Int,
    habitsTotal: Int,
    cycleDayInfo: CycleDayInfo?,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {
            ritmBanner(
                title = "Сегодня",
                subtitle = "Пять ритмов в одном дне: вода, шаги, привычки, цикл и голодание.",
                rhythm = RitmRhythm.Today,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                rhythmOrb(
                    label = "вода",
                    value = if (waterGoal > 0) "$waterCount/$waterGoal" else "$waterCount",
                    rhythm = RitmRhythm.Water,
                    modifier = Modifier.weight(1f),
                )
                rhythmOrb(
                    label = "шаги",
                    value = if (stepsState.fallback == null) "${stepsState.todaySteps}" else "—",
                    rhythm = RitmRhythm.Steps,
                    modifier = Modifier.weight(1f),
                )
                rhythmOrb(
                    label = "голод",
                    value = if (isFastingActive) "идёт" else "нет",
                    rhythm = RitmRhythm.Fasting,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                rhythmOrb(
                    label = "привычки",
                    value = "$habitsDone/$habitsTotal",
                    rhythm = RitmRhythm.Habits,
                    modifier = Modifier.weight(1f),
                )
                rhythmOrb(
                    label = "цикл",
                    value = cycleDayInfo?.dayOfCycle?.takeIf { it > 0 }?.toString() ?: "—",
                    rhythm = RitmRhythm.Cycle,
                    modifier = Modifier.weight(1f),
                )
                Column(modifier = Modifier.weight(1f)) {}
            }
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
                    color = WaterAccent,
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
                habits.take(MAX_TODAY_HABITS).forEach { habit ->
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
                val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMs) % MINUTES_PER_HOUR
                val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMs) % SECONDS_PER_MINUTE
                Text(
                    text = "Осталось: %02d:%02d:%02d".format(hours, minutes, seconds),
                    style = MaterialTheme.typography.bodyLarge,
                    color = FastingAccent,
                )
                ritmButton(
                    text = "Сменить окно",
                    onClick = onStartClick,
                )
            } else if (isActive) {
                Text(
                    text = "Сессия активна",
                    style = MaterialTheme.typography.bodyMedium,
                )
                ritmButton(
                    text = "Сменить окно",
                    onClick = onStartClick,
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

private fun CyclePhase.displayName(): String =
    when (this) {
        CyclePhase.MENSTRUAL -> "Менструальная"
        CyclePhase.FOLLICULAR -> "Фолликулярная"
        CyclePhase.OVULATION -> "Овуляция"
        CyclePhase.LUTEAL -> "Лютеиновая"
        CyclePhase.UNKNOWN -> "Неизвестно"
    }

// ── T-08: Steps ───────────────────────────────────────────────────────────────
