package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun habitDetailScreen(
    onBack: () -> Unit,
    onArchived: () -> Unit,
    viewModel: HabitDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.habit?.title ?: "") },
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
            contentPadding = PaddingValues(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            item {
                ritmCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = state.habit?.iconEmoji ?: "✅",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))
                        Column {
                            Text(
                                text = "🔥 ${state.currentStreak} дней подряд",
                                style = MaterialTheme.typography.titleMedium,
                                color = HabitsAccent,
                            )
                            Text(
                                text = "Всего отметок: ${state.checks.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "История",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.xs),
                )
            }

            if (state.checks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.lg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Пока нет отметок",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                items(state.checks, key = { it.id }) { check ->
                    ritmCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.md),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(check.date, style = MaterialTheme.typography.bodyMedium)
                            Text("✅", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.lg))
                ritmOutlinedButton(
                    text = "Архивировать привычку",
                    onClick = {
                        viewModel.archive()
                        onArchived()
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
