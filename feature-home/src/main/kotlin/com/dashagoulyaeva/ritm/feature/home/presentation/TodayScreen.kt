package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.RitmCardShape
import com.dashagoulyaeva.ritm.core.ui.components.ritmTodayCard
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.CycleSoft
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.FastingSoft
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.StepsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.StepsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.WaterAccent
import com.dashagoulyaeva.ritm.core.ui.theme.WaterSoft
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import com.dashagoulyaeva.ritm.feature.fasting.presentation.fastingBottomSheet
import com.dashagoulyaeva.ritm.feature.water.presentation.WaterViewModel
import com.dashagoulyaeva.ritm.feature.water.presentation.waterQuickLogBottomSheet
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

private const val STEPS_GOAL = 10_000
private const val CYCLE_LEN = 28
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
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showWaterSheet() }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить воду")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // ── HERO: dark banner + pulse orb ─────────────────────────────
            item {
                todayHero(
                    waterCount    = waterState.todayCount,
                    waterGoal     = waterState.dailyGoal,
                    stepsState    = stepsState,
                    isFastingActive = today.activeFastingSession != null,
                    habitsDone    = today.habitCheckedIds.size,
                    habitsTotal   = today.habits.size,
                    cycleDayInfo  = today.cycleDayInfo,
                )
            }

            // ── COMPACT RHYTHM CARDS ───────────────────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    // Цикл
                    val cycleValue = today.cycleDayInfo?.let {
                        if (it.dayOfCycle > 0) it.phase.displayName() else "нет данных"
                    } ?: "нет данных"
                    val cycleSub = today.cycleDayInfo?.let {
                        if (it.dayOfCycle > 0) "день ${it.dayOfCycle} из $CYCLE_LEN" else null
                    }
                    ritmTodayCard(
                        accentColor = CycleAccent,
                        title = "цикл",
                        value = cycleValue,
                        sub = cycleSub,
                        softColor = CycleSoft,
                        active = today.cycleDayInfo?.dayOfCycle?.let { it > 0 } ?: false,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    // Голодание
                    val fastingActive = today.activeFastingSession != null
                    val fastingValue = if (fastingActive && today.fastingRemainingMs != null) {
                        val h = TimeUnit.MILLISECONDS.toHours(today.fastingRemainingMs)
                        val m = TimeUnit.MILLISECONDS.toMinutes(today.fastingRemainingMs) % MINUTES_PER_HOUR
                        "%dч %02dм осталось".format(h, m)
                    } else if (fastingActive) {
                        "идёт"
                    } else {
                        "не активно"
                    }
                    ritmTodayCard(
                        accentColor = FastingAccent,
                        title = "голодание",
                        value = fastingValue,
                        sub = if (fastingActive) "нажми чтобы изменить окно" else "начать сессию",
                        softColor = FastingSoft,
                        active = fastingActive,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.showFastingSheet() },
                    )

                    // Вода
                    val waterPct = if (waterState.dailyGoal > 0)
                        (waterState.todayCount * 100 / waterState.dailyGoal) else 0
                    ritmTodayCard(
                        accentColor = WaterAccent,
                        title = "вода",
                        value = "${waterState.todayCount} / ${waterState.dailyGoal}",
                        sub = "выпито стаканов · $waterPct%",
                        softColor = WaterSoft,
                        active = waterState.todayCount > 0,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onOpenWaterHistory,
                        bottom = {
                            if (waterState.dailyGoal > 0) {
                                Spacer(Modifier.height(5.dp))
                                waterMiniBar(
                                    count = waterState.todayCount,
                                    goal  = waterState.dailyGoal,
                                )
                            }
                        },
                    )

                    // Шаги
                    val stepsStr = if (stepsState.fallback == null) {
                        "%,d".format(stepsState.todaySteps).replace(',', ' ')
                    } else "—"
                    val stepsGoalPct = if (stepsState.fallback == null)
                        (stepsState.todaySteps * 100 / STEPS_GOAL).coerceIn(0, 100) else 0
                    ritmTodayCard(
                        accentColor = StepsAccent,
                        title = "шаги",
                        value = stepsStr,
                        sub = if (stepsState.fallback == null) {
                        "цель $STEPS_GOAL · $stepsGoalPct%"
                    } else when (stepsState.fallback) {
                        StepsFallback.PermissionDenied  -> "нет разрешения на шаги"
                        StepsFallback.SensorUnavailable -> "датчик недоступен"
                        StepsFallback.NoDataYet         -> "ещё нет данных"
                        null                            -> null
                    },
                        softColor = StepsSoft,
                        active = stepsState.todaySteps > 0,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onStepsHistoryClick,
                    )

                    // Привычки
                    val habitsTotal = today.habits.size
                    val habitsDone  = today.habitCheckedIds.size
                    ritmTodayCard(
                        accentColor = HabitsAccent,
                        title = "привычки",
                        value = "$habitsDone / $habitsTotal",
                        sub = if (habitsTotal == 0) "добавь привычки в разделе Привычки"
                              else if (habitsDone == habitsTotal && habitsTotal > 0) "все выполнены"
                              else "осталось ${habitsTotal - habitsDone}",
                        softColor = HabitsSoft,
                        active = habitsDone > 0,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
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
            fastingBottomSheet(onDismiss = { viewModel.hideFastingSheet() })
        }
    }
}

// ── Hero ──────────────────────────────────────────────────────────────────────

@Suppress("LongParameterList", "LongMethod")
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
    val waterP   = if (waterGoal > 0) (waterCount.toFloat() / waterGoal).coerceIn(0f, 1f) else 0f
    val stepsP   = (stepsState.todaySteps.toFloat() / STEPS_GOAL).coerceIn(0f, 1f)
    val habitsP  = if (habitsTotal > 0) (habitsDone.toFloat() / habitsTotal).coerceIn(0f, 1f) else 0f
    val fastingP = if (isFastingActive) 0.58f else 0f
    val cycleP   = cycleDayInfo?.takeIf { it.dayOfCycle > 0 }
        ?.let { (it.dayOfCycle.toFloat() / CYCLE_LEN).coerceIn(0f, 1f) } ?: 0f

    val mood = computeMood(waterP, stepsP, habitsP)

    val todayDate = LocalDate.now()
    val dayName = todayDate.dayOfWeek
        .getDisplayName(java.time.format.TextStyle.SHORT, Locale("ru"))
        .replaceFirstChar { it.titlecase() }
    val dateStr = todayDate.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
    val cycleDay = cycleDayInfo?.takeIf { it.dayOfCycle > 0 }?.dayOfCycle
    val kicker = buildString {
        append("$dayName · $dateStr")
        if (cycleDay != null) append(" · день $cycleDay")
    }

    Column(modifier = modifier) {
        // Dark banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(
                    topStart = 0.dp, topEnd = 0.dp,
                    bottomStart = 20.dp, bottomEnd = 8.dp,
                ))
                .background(Color(0xFF1C1B1F))
                .padding(horizontal = 18.dp, vertical = 20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = kicker.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.55f),
                    letterSpacing = 1.sp,
                )
                Text(
                    text = "ритм дня",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
                Text(
                    text = mood.word,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = mood.color,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = mood.hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f),
                )
            }
        }

        // Pulse orb
        rhythmPulseOrb(
            cycleP   = cycleP,
            fastingP = fastingP,
            waterP   = waterP,
            stepsP   = stepsP,
            habitsP  = habitsP,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
        )

        // Scroll hint
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "ПЯТЬ РИТМОВ НИЖЕ",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.2.sp,
            )
            Text(
                text = "↓",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Pulse Orb — 5 concentric arcs ────────────────────────────────────────────

@Suppress("LongParameterList")
@Composable
private fun rhythmPulseOrb(
    cycleP: Float,
    fastingP: Float,
    waterP: Float,
    stepsP: Float,
    habitsP: Float,
    modifier: Modifier = Modifier,
) {
    var timeStr by remember { mutableStateOf(currentTime()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            timeStr = currentTime()
        }
    }

    // Outer → inner: cycle, fasting, steps, habits, water
    val rhythms = listOf(
        CycleAccent   to cycleP,
        FastingAccent to fastingP,
        StepsAccent   to stepsP,
        HabitsAccent  to habitsP,
        WaterAccent   to waterP,
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx    = size.width / 2f
            val cy    = size.height / 2f
            val maxR  = minOf(size.width, size.height) / 2f - 14.dp.toPx()
            val minR  = maxR * 0.30f
            val step  = if (rhythms.size > 1) (maxR - minR) / (rhythms.size - 1) else 0f
            val sw    = 7.dp.toPx()
            val dotR  = sw / 2f + 3.5.dp.toPx()

            // Arc starts at -90° (top) and sweeps clockwise
            val startAngle = -90f
            val sweepMax   = 320f   // leaves ~40° gap at top-left

            rhythms.forEachIndexed { i, (color, progress) ->
                val r    = maxR - i * step
                val left = cx - r
                val top  = cy - r
                val arcSize = Size(r * 2, r * 2)

                // Faint track
                drawArc(
                    color = color.copy(alpha = 0.13f),
                    startAngle = startAngle,
                    sweepAngle = sweepMax,
                    useCenter = false,
                    topLeft = Offset(left, top),
                    size = arcSize,
                    style = Stroke(width = sw, cap = StrokeCap.Round),
                )

                val p  = progress.coerceIn(0f, 1f)
                val sw2 = p * sweepMax

                // Progress arc
                if (sw2 > 0.5f) {
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sw2,
                        useCenter = false,
                        topLeft = Offset(left, top),
                        size = arcSize,
                        style = Stroke(width = sw, cap = StrokeCap.Round),
                    )
                }

                // Dot at arc end
                val angleDeg = startAngle + sw2
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val dotX = cx + r * cos(angleRad).toFloat()
                val dotY = cy + r * sin(angleRad).toFloat()
                drawCircle(color = color,                    radius = dotR,           center = Offset(dotX, dotY))
                drawCircle(color = Color.White,              radius = dotR - 2.dp.toPx(), center = Offset(dotX, dotY))
                drawCircle(color = color.copy(alpha = 0.8f), radius = dotR - 4.dp.toPx(), center = Offset(dotX, dotY))
            }
        }

        // Centre text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timeStr,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.03).sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "пульс дня",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
            )
        }
    }
}

// ── Water mini bar ────────────────────────────────────────────────────────────

@Composable
private fun waterMiniBar(count: Int, goal: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(goal.coerceAtMost(12)) { i ->
            val filled = i < count
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RitmCardShape)
                    .background(
                        if (filled) WaterAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    ),
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private data class MoodInfo(val word: String, val color: Color, val hint: String)

private fun computeMood(waterP: Float, stepsP: Float, habitsP: Float): MoodInfo {
    val avg = listOf(waterP, stepsP, habitsP).average().toFloat()
    return when {
        avg >= 0.65f -> MoodInfo("отличный",   HabitsAccent,  "всё идёт по плану · продолжай")
        avg >= 0.35f -> MoodInfo("ровный",     FastingAccent, "фокус: держать ритм без перегруза")
        avg >= 0.05f -> MoodInfo("активный",   WaterAccent,   "хорошее начало · первый шаг сделан")
        else         -> MoodInfo("начало дня", StepsAccent,   "первый шаг важнее всего")
    }
}

private fun currentTime(): String {
    val now = LocalTime.now()
    return "%02d:%02d".format(now.hour, now.minute)
}

private fun CyclePhase.displayName(): String = when (this) {
    CyclePhase.MENSTRUAL  -> "менструация"
    CyclePhase.FOLLICULAR -> "фолликулярная"
    CyclePhase.OVULATION  -> "день овуляции"
    CyclePhase.LUTEAL     -> "лютеиновая"
    CyclePhase.UNKNOWN    -> "нет данных"
}
