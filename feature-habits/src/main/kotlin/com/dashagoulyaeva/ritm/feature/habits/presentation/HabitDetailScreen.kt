package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.StepsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import java.time.LocalDate

private const val HEATMAP_DAYS = 42
private const val HEATMAP_COLS = 7
private const val DAYS_30 = 30
private const val DAYS_31 = 31

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun habitDetailScreen(
    onBack: () -> Unit,
    onArchived: () -> Unit,
    viewModel: HabitDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    // Pre-compute heatmap data
    val today = LocalDate.now().toString()
    val past42Days = (HEATMAP_DAYS - 1 downTo 0).map {
        LocalDate.now().minusDays(it.toLong()).toString()
    }
    val doneDates = state.checks.map { it.date }.toSet()

    // 30-day rate
    val total30 = state.checks.count {
        runCatching {
            LocalDate.parse(it.date).isAfter(LocalDate.now().minusDays(DAYS_31.toLong()))
        }.getOrDefault(false)
    }
    val rate30 = if (total30 > 0) "${(total30 * 100 / DAYS_30).coerceAtMost(100)}%" else "—"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.habit?.title ?: "Привычка") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = MaterialTheme.spacing.lg),
        ) {

            // ── 1. DARK HERO BANNER ────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 8.dp))
                        .background(Color(0xFF1C1B1F))
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                ) {
                    Column {
                        Text(
                            text = "ПРИВЫЧКА",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.55f),
                        )
                        Text(
                            text = state.habit?.title ?: "загрузка...",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.White,
                        )
                        Text(
                            text = "делать · ежедневно",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.65f),
                        )
                    }
                }
            }

            // ── 2. 3 STATS CARDS ──────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Card 1: streak
                    ritmCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "${state.currentStreak}",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = if (state.currentStreak > 0) HabitsAccent else InkOnLight,
                            )
                            Text(
                                text = "СТРИК",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkOnLight.copy(alpha = 0.55f),
                            )
                        }
                    }

                    // Card 2: 30-day rate
                    ritmCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = rate30,
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = InkOnLight,
                            )
                            Text(
                                text = "ЗА 30 ДНЕЙ",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkOnLight.copy(alpha = 0.55f),
                            )
                        }
                    }

                    // Card 3: total
                    ritmCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "${state.checks.size}",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = InkOnLight,
                            )
                            Text(
                                text = "ВСЕГО",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkOnLight.copy(alpha = 0.55f),
                            )
                        }
                    }
                }
            }

            // ── 3. SECTION HEADER ─────────────────────────────────────────
            item {
                Text(
                    text = "6 НЕДЕЛЬ",
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 0.8.sp,
                    color = InkOnLight.copy(alpha = 0.55f),
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp, bottom = 6.dp),
                )
            }

            // ── 4. HEATMAP GRID ───────────────────────────────────────────
            item {
                val surfaceColor = MaterialTheme.colorScheme.surface
                LazyVerticalGrid(
                    columns = GridCells.Fixed(HEATMAP_COLS),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(176.dp),
                    userScrollEnabled = false,
                ) {
                    items(past42Days) { dateStr ->
                        val isDone = dateStr in doneDates
                        val isToday = dateStr == today
                        val isFuture = dateStr > today

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .then(
                                    when {
                                        isDone -> Modifier.background(HabitsAccent)
                                        isToday -> Modifier.background(StepsSoft)
                                        isFuture -> Modifier
                                            .alpha(0.2f)
                                            .background(surfaceColor)
                                        else -> Modifier
                                            .alpha(0.35f)
                                            .background(surfaceColor)
                                            .border(
                                                width = 1.dp,
                                                color = InkOnLight.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp),
                                            )
                                    }
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (isToday) {
                                Text(
                                    text = "?",
                                    fontSize = 10.sp,
                                    color = InkOnLight,
                                )
                            }
                        }
                    }
                }

                // Legend
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(HabitsAccent),
                    )
                    Text(
                        text = "выполнено",
                        fontSize = 11.sp,
                        color = InkOnLight.copy(alpha = 0.6f),
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(InkOnLight.copy(alpha = 0.15f)),
                    )
                    Text(
                        text = "пропущено",
                        fontSize = 11.sp,
                        color = InkOnLight.copy(alpha = 0.6f),
                    )
                }
            }

            // ── 5. ARCHIVE BUTTON ─────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.lg))
                ritmOutlinedButton(
                    text = "Архивировать привычку",
                    onClick = {
                        viewModel.archive()
                        onArchived()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                )
            }
        }
    }
}
