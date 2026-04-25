package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun stepsTodayWidget(
    state: StepsUiState,
    onHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (!state.isAvailable) {
                Text(
                    text = "Шагомер недоступен",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "Шаги сегодня",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${state.todaySteps} / ${state.dailyGoal}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(onClick = onHistoryClick) {
                    Text("История")
                }
            }
        }
    }
}
