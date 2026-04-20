package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@Composable
fun waterTodayWidget(
    uiState: WaterUiState,
    onQuickLogClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Text(
                text = "Вода",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Сегодня: ${uiState.todayCount}/${uiState.dailyGoal} стаканов",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Прогресс: ${(uiState.progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color =
                    if (uiState.isGoalReached) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                ritmButton(
                    text = "Быстрый лог",
                    onClick = onQuickLogClick,
                )
                ritmButton(
                    text = "История",
                    onClick = onHistoryClick,
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
