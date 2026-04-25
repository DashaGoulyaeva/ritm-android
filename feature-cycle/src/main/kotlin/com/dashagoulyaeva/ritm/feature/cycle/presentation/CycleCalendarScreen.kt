package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private const val DAYS_IN_WEEK = 7
private const val PERIOD_ALPHA = 0.3f
private const val TODAY_BORDER_WIDTH = 2
private const val LOG_DOT_SIZE = 4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cycleCalendarScreen(
    onDayClick: (String) -> Unit,
    cycleViewModel: CycleViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    val cycleState by cycleViewModel.uiState.collectAsState()
    val calState by calendarViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Цикл") })
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
        ) {
            cycleDayHeader(cycleState)
            cyclePeriodButton(cycleState, cycleViewModel)
            if (calState.year != 0 && calState.month != 0) {
                cycleMonthCalendar(calState, onDayClick)
            }
        }
    }
}

@Composable
private fun cycleDayHeader(cycleState: CycleUiState) {
    val dayInfo = cycleState.currentDayInfo
    val currentDayText =
        when {
            dayInfo.dayOfCycle == 0 -> "Нет данных о цикле"
            dayInfo.isInPeriod -> "День цикла ${dayInfo.dayOfCycle} · Менструация"
            else -> "День цикла ${dayInfo.dayOfCycle} · ${dayInfo.phase.russianName()}"
        }
    Text(
        text = currentDayText,
        color = CycleAccent,
        style = MaterialTheme.typography.titleMedium,
    )
    val predicted = cycleState.predictedNextPeriod
    if (predicted != null) {
        Text(
            text = "Следующий цикл: $predicted",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun cyclePeriodButton(
    cycleState: CycleUiState,
    cycleViewModel: CycleViewModel,
) {
    if (cycleState.activePeriod == null) {
        ritmButton(
            text = "Начать менструацию",
            onClick = cycleViewModel::startPeriod,
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        ritmOutlinedButton(
            text = "Завершить менструацию",
            onClick = cycleViewModel::endPeriod,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun cycleMonthCalendar(
    calState: CalendarUiState,
    onDayClick: (String) -> Unit,
) {
    val yearMonth = YearMonth.of(calState.year, calState.month)
    val monthName =
        yearMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.uppercase() }
    Text(
        text = "$monthName ${calState.year}",
        style = MaterialTheme.typography.titleSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    calendarWeekHeader()

    val today = LocalDate.now()
    val periodDates = buildPeriodDates(calState, today)
    val logDates = calState.logs.map { it.date }.toSet()

    val firstDayOfMonth = yearMonth.atDay(1)
    val startOffset = firstDayOfMonth.dayOfWeek.value - 1
    val daysInMonth = yearMonth.lengthOfMonth()
    val gridItems =
        buildList {
            repeat(startOffset) { add(null) }
            for (day in 1..daysInMonth) {
                add(yearMonth.atDay(day))
            }
        }

    LazyVerticalGrid(columns = GridCells.Fixed(DAYS_IN_WEEK)) {
        items(gridItems) { date ->
            calendarDayCell(date, today, periodDates, logDates, onDayClick)
        }
    }
}

@Composable
private fun calendarWeekHeader() {
    val dayHeaders = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    Row(modifier = Modifier.fillMaxWidth()) {
        dayHeaders.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun buildPeriodDates(
    calState: CalendarUiState,
    today: LocalDate,
): Set<String> {
    val periodDates = mutableSetOf<String>()
    for (period in calState.periods) {
        val start = LocalDate.parse(period.startDate)
        val end = if (period.endDate != null) LocalDate.parse(period.endDate) else today
        var current = start
        while (!current.isAfter(end)) {
            periodDates.add(current.toString())
            current = current.plusDays(1)
        }
    }
    return periodDates
}

@Composable
private fun calendarDayCell(
    date: LocalDate?,
    today: LocalDate,
    periodDates: Set<String>,
    logDates: Set<String>,
    onDayClick: (String) -> Unit,
) {
    if (date == null) {
        Box(modifier = Modifier.aspectRatio(1f))
        return
    }
    val dateString = date.toString()
    val isInPeriod = periodDates.contains(dateString)
    val isToday = date == today
    val hasLog = logDates.contains(dateString)

    Box(
        modifier =
            Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .then(
                    if (isInPeriod) Modifier.background(CycleAccent.copy(alpha = PERIOD_ALPHA))
                    else Modifier,
                )
                .then(
                    if (isToday) Modifier.border(TODAY_BORDER_WIDTH.dp, CycleAccent, CircleShape)
                    else Modifier,
                )
                .clickable { onDayClick(dateString) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
        )
        if (hasLog) {
            Box(
                modifier =
                    Modifier
                        .size(LOG_DOT_SIZE.dp)
                        .background(CycleAccent, CircleShape)
                        .align(Alignment.BottomCenter),
            )
        }
    }
}

private fun CyclePhase.russianName(): String =
    when (this) {
        CyclePhase.MENSTRUAL -> "Менструальная"
        CyclePhase.FOLLICULAR -> "Фолликулярная"
        CyclePhase.OVULATION -> "Овуляция"
        CyclePhase.LUTEAL -> "Лютеиновая"
        CyclePhase.UNKNOWN -> "Неизвестно"
    }
