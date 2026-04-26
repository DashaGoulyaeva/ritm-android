package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.RitmCardShape
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun habitsScreen(
    onHabitClick: (Long) -> Unit,
    viewModel: HabitsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var showCreateSheet by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("today") }

    val displayedHabits = when (filter) {
        "all" -> state.habits
        else -> state.habits // "today" shows all active habits with today check state
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = HabitsAccent,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить привычку")
            }
        },
    ) { padding ->
        if (displayedHabits.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✅", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))
                    Text(
                        text = "нет привычек",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "нажми + чтобы добавить",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                // 1. HERO BANNER
                item {
                    heroBanner(
                        checkedCount = state.todayCheckedIds.size,
                        totalCount = state.habits.size,
                    )
                }

                // 2. FILTER ROW
                item {
                    filterRow(
                        filter = filter,
                        onFilterChange = { filter = it },
                    )
                }

                // 3. HABIT ITEMS
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        displayedHabits.forEach { habit ->
                            habitListItem(
                                habit = habit,
                                isChecked = state.todayCheckedIds.contains(habit.id),
                                onCheckedChange = { checked -> viewModel.toggleHabit(habit.id, checked) },
                                onClick = { onHabitClick(habit.id) },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCreateSheet) {
        createHabitBottomSheet(
            onDismiss = { showCreateSheet = false },
            onConfirm = { title, emoji ->
                viewModel.createNewHabit(title, emoji)
                showCreateSheet = false
            },
        )
    }
}

@Composable
private fun heroBanner(
    checkedCount: Int,
    totalCount: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 8.dp,
                ),
            )
            .background(Color(0xFF1C1B1F))
            .padding(horizontal = 18.dp, vertical = 20.dp),
    ) {
        Column {
            Text(
                text = "ПРИВЫЧКИ",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$checkedCount из $totalCount",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color.White,
            )
            Text(
                text = "на сегодня",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                ),
                color = HabitsAccent,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$totalCount активных",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun filterRow(
    filter: String,
    onFilterChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = filter == "today",
            onClick = { onFilterChange("today") },
            label = { Text("сегодня") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = HabitsAccent.copy(alpha = 0.15f),
                selectedLabelColor = HabitsAccent,
            ),
        )
        FilterChip(
            selected = filter == "all",
            onClick = { onFilterChange("all") },
            label = { Text("все") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = HabitsAccent.copy(alpha = 0.15f),
                selectedLabelColor = HabitsAccent,
            ),
        )
    }
}

@Composable
private fun habitListItem(
    habit: Habit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ritmCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RitmCardShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            Row {
                // Left accent stripe
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(HabitsAccent),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = habit.iconEmoji,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = habit.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                        )
                        Text(
                            text = if (isChecked) "выполнено" else "нажми чтобы отметить",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkOnLight.copy(alpha = 0.55f),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = onCheckedChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = HabitsAccent,
                        ),
                    )
                }
            }
        }
    }
}
