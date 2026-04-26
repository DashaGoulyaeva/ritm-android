package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.FlowIntensity
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.MoodLevel

private fun flowIntensityLabel(flow: FlowIntensity): String =
    when (flow) {
        FlowIntensity.NONE -> "нет"
        FlowIntensity.LIGHT -> "легко"
        FlowIntensity.MEDIUM -> "средне"
        FlowIntensity.HEAVY -> "обильно"
    }

private fun moodLevelLabel(mood: MoodLevel): String =
    when (mood) {
        MoodLevel.GREAT -> "супер"
        MoodLevel.GOOD -> "хорошо"
        MoodLevel.NEUTRAL -> "норм"
        MoodLevel.LOW -> "так-сё"
        MoodLevel.AWFUL -> "плохо"
        MoodLevel.UNKNOWN -> ""
    }

private const val NOTE_MIN_LINES = 3

private val symptoms =
    listOf(
        "боль" to "боль",
        "усталость" to "усталость",
        "вздутие" to "вздутие",
        "головная боль" to "головная_боль",
        "тошнота" to "тошнота",
        "перепады настроения" to "перепады_настроения",
        "акне" to "акне",
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
            journalFlowSection(state, viewModel)
            journalSymptomsSection(state, viewModel)
            journalMoodSection(state, viewModel)
            journalNoteSection(state, viewModel)
        }
    }
}

@Composable
private fun sectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        letterSpacing = 0.8.sp,
        color = InkOnLight.copy(alpha = 0.55f),
        modifier = Modifier.padding(top = MaterialTheme.spacing.md, bottom = 4.dp),
    )
}

@Composable
private fun journalFlowSection(
    state: JournalUiState,
    viewModel: JournalViewModel,
) {
    sectionHeader("Выделения")
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        modifier = Modifier.fillMaxWidth(),
    ) {
        FlowIntensity.entries.forEach { flow ->
            val selected = state.flow == flow
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) InkOnLight else MaterialTheme.colorScheme.surface,
                ),
                border = if (selected) null else BorderStroke(1.2.dp, InkOnLight.copy(alpha = 0.14f)),
                onClick = { viewModel.setFlow(flow) },
            ) {
                Text(
                    text = flowIntensityLabel(flow),
                    color = if (selected) Color.White else InkOnLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = SpaceGrotesk,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun journalSymptomsSection(
    state: JournalUiState,
    viewModel: JournalViewModel,
) {
    sectionHeader("Симптомы")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
        symptoms.forEach { (label, key) ->
            FilterChip(
                selected = state.symptoms.contains(key),
                onClick = { viewModel.toggleSymptom(key) },
                label = { Text(label) },
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CycleAccent.copy(alpha = 0.15f),
                        selectedLabelColor = CycleAccent,
                    ),
            )
        }
    }
}

@Composable
private fun journalMoodSection(
    state: JournalUiState,
    viewModel: JournalViewModel,
) {
    sectionHeader("Настроение")
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        modifier = Modifier.fillMaxWidth(),
    ) {
        MoodLevel.entries
            .filter { it != MoodLevel.UNKNOWN }
            .forEach { mood ->
                val selected = state.mood == mood
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { viewModel.setMood(mood) },
                        shape = CircleShape,
                        color = if (selected) CycleAccent else Color.Transparent,
                        border = if (selected) null else BorderStroke(1.2.dp, InkOnLight),
                    ) {}
                    Text(
                        text = moodLevelLabel(mood),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = SpaceGrotesk,
                        color = if (selected) CycleAccent else InkOnLight.copy(alpha = 0.7f),
                    )
                }
            }
    }
}

@Composable
private fun journalNoteSection(
    state: JournalUiState,
    viewModel: JournalViewModel,
) {
    sectionHeader("Заметка")
    OutlinedTextField(
        value = state.note,
        onValueChange = { viewModel.setNote(it) },
        modifier = Modifier.fillMaxWidth(),
        minLines = NOTE_MIN_LINES,
        placeholder = { Text("что сегодня с телом и головой...") },
    )
    Button(
        onClick = { viewModel.save() },
        modifier = Modifier
            .align(Alignment.End)
            .padding(vertical = MaterialTheme.spacing.md),
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CycleAccent),
    ) {
        Text(
            text = "сохранить",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
