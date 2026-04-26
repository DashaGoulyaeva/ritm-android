package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.RitmCardShape
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.CycleSoft
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private const val DAYS_IN_WEEK = 7
private const val LOG_DOT_SIZE = 5

@Composable
fun cycleCalendarScreen(
    onDayClick: (String) -> Unit,
    cycleViewModel: CycleViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    val cycleState by cycleViewModel.uiState.collectAsState()
    val calState by calendarViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            // 1. HERO BANNER
            item { cycleHeroBanner(cycleState) }

            // 2. RHYTHM ORB
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    cycleOrbSection(cycleState)
                }
            }

            // 3. MONTH NAVIGATION (name only, no prev/next — CalendarViewModel has no navigation)
            item { cycleMonthName(calState) }

            // 4. CALENDAR GRID
            if (calState.year != 0) {
                item { cycleCalendarGrid(calState, onDayClick) }
            }

            // 5. LEGEND
            item { cycleLegend() }

            // 6. QUICK ACTIONS
            item { cycleQuickActions(cycleState, cycleViewModel) }

            // 7. FORECAST
            item { cycleForecast(cycleState) }
        }
    }
}

// ---------------------------------------------------------------------------
// 1. HERO BANNER
// ---------------------------------------------------------------------------

@Composable
private fun cycleHeroBanner(cycleState: CycleUiState) {
    val monthName = java.time.LocalDate.now()
        .month
        .getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        .replaceFirstChar { it.uppercase() }

    val phaseDisplayName = when (cycleState.currentDayInfo.phase) {
        CyclePhase.MENSTRUAL -> "менструация"
        CyclePhase.FOLLICULAR -> "фолликулярная"
        CyclePhase.OVULATION -> "день овуляции"
        CyclePhase.LUTEAL -> "лютеиновая"
        CyclePhase.UNKNOWN -> "нет данных"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1C1B1F),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 8.dp,
                ),
            )
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 8.dp,
                ),
            )
            .padding(horizontal = 18.dp, vertical = 20.dp),
    ) {
        Column {
            Text(
                text = "ЦИКЛ · $monthName".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (cycleState.currentDayInfo.phase != CyclePhase.UNKNOWN) {
                    phaseDisplayName
                } else {
                    "нет данных"
                },
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cycleState.predictedNextPeriod
                    ?.let { "следующая ~ $it" }
                    ?: "данных о цикле нет",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// 2. ORB SECTION
// ---------------------------------------------------------------------------

@Composable
private fun cycleOrbSection(cycleState: CycleUiState) {
    val dayOfCycle = cycleState.currentDayInfo.dayOfCycle
    val isInPeriod = cycleState.currentDayInfo.isInPeriod

    Card(
        modifier = Modifier.size(160.dp),
        shape = RitmCardShape,
        colors = CardDefaults.cardColors(containerColor = CycleSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (dayOfCycle != 0) "Я$dayOfCycle/28" else "—/28",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = CycleAccent,
                )
                Text(
                    text = "день цикла",
                    style = MaterialTheme.typography.labelSmall,
                    color = InkOnLight.copy(alpha = 0.6f),
                )
                if (isInPeriod) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(CycleAccent, CircleShape),
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 3. MONTH NAME (no navigation — CalendarViewModel has no prevMonth/nextMonth)
// ---------------------------------------------------------------------------

@Composable
private fun cycleMonthName(calState: CalendarUiState) {
    if (calState.year == 0 || calState.month == 0) return
    val yearMonth = YearMonth.of(calState.year, calState.month)
    val monthName = yearMonth.month
        .getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        .replaceFirstChar { it.uppercase() }
    Text(
        text = "$monthName '${calState.year % 100}",
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
    )
}

// ---------------------------------------------------------------------------
// 4. CALENDAR GRID
// ---------------------------------------------------------------------------

@Composable
private fun cycleCalendarGrid(
    calState: CalendarUiState,
    onDayClick: (String) -> Unit,
) {
    val yearMonth = YearMonth.of(calState.year, calState.month)
    val today = LocalDate.now()
    val periodDates = buildPeriodDates(calState, today)
    val logDates = calState.logs.map { it.date }.toSet()

    val firstDayOfMonth = yearMonth.atDay(1)
    val startOffset = firstDayOfMonth.dayOfWeek.value - 1
    val daysInMonth = yearMonth.lengthOfMonth()
    val gridItems = buildList {
        repeat(startOffset) { add(null) }
        for (day in 1..daysInMonth) {
            add(yearMonth.atDay(day))
        }
    }

    Column(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)) {
        calendarWeekHeader()
        // Use a fixed-height grid instead of lazy to avoid nested scroll conflict
        val rows = ((gridItems.size + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK)
        val cellSize = 44.dp
        Column {
            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until DAYS_IN_WEEK) {
                        val index = row * DAYS_IN_WEEK + col
                        val date = gridItems.getOrNull(index)
                        Box(modifier = Modifier.weight(1f)) {
                            calendarDayCell(date, today, periodDates, logDates, onDayClick)
                        }
                    }
                }
            }
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
                color = InkOnLight.copy(alpha = 0.5f),
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

    val bgColor = when {
        isInPeriod -> CycleAccent
        isToday -> Color(0xFF1C1B1F)
        else -> Color.Transparent
    }
    val textColor = when {
        isInPeriod || isToday -> Color.White
        else -> InkOnLight
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(bgColor, CircleShape)
            .clickable { onDayClick(dateString) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            textAlign = TextAlign.Center,
        )
        if (hasLog) {
            Box(
                modifier = Modifier
                    .size(LOG_DOT_SIZE.dp)
                    .background(if (isInPeriod || isToday) Color.White else CycleAccent, CircleShape)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// 5. LEGEND
// ---------------------------------------------------------------------------

@Composable
private fun cycleLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        legendItem(color = CycleAccent, label = "менструация")
        legendItem(color = InkOnLight, label = "сегодня")
    }
}

@Composable
private fun legendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, CircleShape),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkOnLight.copy(alpha = 0.7f),
        )
    }
}

// ---------------------------------------------------------------------------
// 6. QUICK ACTIONS
// ---------------------------------------------------------------------------

@Composable
private fun cycleQuickActions(
    cycleState: CycleUiState,
    cycleViewModel: CycleViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (cycleState.activePeriod == null) {
            Button(
                onClick = cycleViewModel::startPeriod,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CycleAccent),
            ) {
                Text(
                    text = "+ начало менструации",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                )
            }
        } else {
            OutlinedButton(
                onClick = cycleViewModel::endPeriod,
                shape = RoundedCornerShape(999.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CycleAccent),
            ) {
                Text(
                    text = "завершить",
                    style = MaterialTheme.typography.labelMedium,
                    color = CycleAccent,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 7. FORECAST
// ---------------------------------------------------------------------------

@Composable
private fun cycleForecast(cycleState: CycleUiState) {
    val predicted = cycleState.predictedNextPeriod ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
        shape = RitmCardShape,
        colors = CardDefaults.cardColors(containerColor = CycleSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = predicted,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = InkOnLight,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "по последним циклам · без медицинских обещаний",
                style = MaterialTheme.typography.bodySmall,
                color = InkOnLight.copy(alpha = 0.6f),
            )
        }
    }
}
