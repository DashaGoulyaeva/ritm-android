package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.FlowIntensity
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.MoodLevel

private fun flowIntensityLabel(flow: FlowIntensity): String =
    when (flow) {
        FlowIntensity.NONE -> "Нет"
        FlowIntensity.LIGHT -> "Лёгкие"
        FlowIntensity.MEDIUM -> "Средние"
        FlowIntensity.HEAVY -> "Обильные"
    }

private fun moodLevelLabel(mood: MoodLevel): String =
    when (mood) {
        MoodLevel.GREAT -> "Отлично"
        MoodLevel.GOOD -> "Хорошо"
        MoodLevel.NEUTRAL -> "Нейтрально"
        MoodLevel.LOW -> "Плохо"
        MoodLevel.AWFUL -> "Ужасно"
        MoodLevel.UNKNOWN -> ""
    }

private val symptoms =
    listOf(
        "Боль" to "боль",
        "Усталость" to "усталость",
        "Вздутие" to "вздутие",
        "Головная боль" to "головная_боль",
        "Тошнота" to "тошнота",
        "Перепады настроения" to "перепады_настроения",
        "Акне" to "акне",
    )

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
@Suppress("LongMethod")
fun cycleDayJournalScreen(
    date: String,
    onBack: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дневник — $date") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = MaterialTheme.spacing.md)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            // Выделения
            Text(
                text = "Выделения",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                FlowIntensity.entries.forEach { flow ->
                    FilterChip(
                        selected = state.flow == flow,
                        onClick = { viewModel.setFlow(flow) },
                        label = { Text(flowIntensityLabel(flow)) },
                        colors =
                            FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CycleAccent.copy(alpha = 0.2f),
                            ),
                    )
                }
            }

            // Настроение
            Text(
                text = "Настроение",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                MoodLevel.entries
                    .filter { it != MoodLevel.UNKNOWN }
                    .forEach { mood ->
                        FilterChip(
                            selected = state.mood == mood,
                            onClick = { viewModel.setMood(mood) },
                            label = { Text(moodLevelLabel(mood)) },
                            colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CycleAccent.copy(alpha = 0.2f),
                                ),
                        )
                    }
            }

            // Симптомы
            Text(
                text = "Симптомы",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                symptoms.forEach { (label, key) ->
                    FilterChip(
                        selected = state.symptoms.contains(key),
                        onClick = { viewModel.toggleSymptom(key) },
                        label = { Text(label) },
                        colors =
                            FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CycleAccent.copy(alpha = 0.2f),
                            ),
                    )
                }
            }

            // Заметка
            Text(
                text = "Заметка",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
            )
            OutlinedTextField(
                value = state.note,
                onValueChange = { viewModel.setNote(it) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                placeholder = { Text("Как вы себя чувствуете?") },
            )

            ritmButton(
                text = "Сохранить",
                onClick = { viewModel.save() },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.md),
            )
        }
    }
}
