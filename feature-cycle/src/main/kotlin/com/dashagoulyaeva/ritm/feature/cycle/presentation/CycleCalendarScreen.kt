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
import androidx.compose.ui.graphics.Color
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
        ) {
            // 1. Текст текущего дня
            val dayInfo = cycleState.currentDayInfo
            val currentDayText = when {
                dayInfo.dayOfCycle == 0 -> "Нет данных о цикле"
                dayInfo.isInPeriod -> "День цикла ${dayInfo.dayOfCycle} · Менструация"
                else -> "День цикла ${dayInfo.dayOfCycle} · ${dayInfo.phase.russianName()}"
            }
            Text(
                text = currentDayText,
                color = CycleAccent,
                style = MaterialTheme.typography.titleMedium,
            )

            // 2. Предсказание следующего периода
            val predicted = cycleState.predictedNextPeriod
            if (predicted != null) {
                Text(
                    text = "Следующий цикл: $predicted",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // 3. Кнопка периода
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

            // 4. Заголовок месяца
            if (calState.year != 0 && calState.month != 0) {
                val yearMonth = YearMonth.of(calState.year, calState.month)
                val monthName = yearMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
                    .replaceFirstChar { it.uppercase() }
                Text(
                    text = "$monthName ${calState.year}",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                // 5. Шапка дней недели
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

                // 6. Сетка дней
                val firstDayOfMonth = yearMonth.atDay(1)
                val startOffset = firstDayOfMonth.dayOfWeek.value - 1
                val daysInMonth = yearMonth.lengthOfMonth()

                val today = LocalDate.now()

                // Набор дат периодов
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

                // Набор дат с логами
                val logDates = calState.logs.map { it.date }.toSet()

                val gridItems = buildList {
                    repeat(startOffset) { add(null) }
                    for (day in 1..daysInMonth) {
                        add(yearMonth.atDay(day))
                    }
                }

                LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                    items(gridItems) { date ->
                        if (date == null) {
                            Box(modifier = Modifier.aspectRatio(1f))
                        } else {
                            val dateString = date.toString()
                            val isInPeriod = periodDates.contains(dateString)
                            val isToday = date == today
                            val hasLog = logDates.contains(dateString)

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .then(
                                        if (isInPeriod) Modifier.background(CycleAccent.copy(alpha = 0.3f))
                                        else Modifier
                                    )
                                    .then(
                                        if (isToday) Modifier.border(2.dp, CycleAccent, CircleShape)
                                        else Modifier
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
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(CycleAccent, CircleShape)
                                            .align(Alignment.BottomCenter),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun CyclePhase.russianName(): String = when (this) {
    CyclePhase.MENSTRUAL -> "Менструальная"
    CyclePhase.FOLLICULAR -> "Фолликулярная"
    CyclePhase.OVULATION -> "Овуляция"
    CyclePhase.LUTEAL -> "Лютеиновая"
    CyclePhase.UNKNOWN -> "Неизвестно"
}
