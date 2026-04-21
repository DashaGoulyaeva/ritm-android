package com.dashagoulyaeva.ritm.feature.fasting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmTonalButton
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@Composable
fun fastingTimerWidget(
    onStartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FastingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ritmCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(MaterialTheme.spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🍽️",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.padding(horizontal = MaterialTheme.spacing.xs))
                Text(
                    text = "Голодание",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.height(MaterialTheme.spacing.sm))

            if (state.activeSession != null) {
                val remaining = state.remainingMs ?: 0L
                val hours = remaining / 3_600_000L
                val minutes = (remaining % 3_600_000L) / 60_000L

                Text(
                    text = if (remaining > 0) "Осталось: %02d:%02d".format(hours, minutes)
                    else "Время вышло!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = FastingAccent,
                )

                Spacer(Modifier.height(MaterialTheme.spacing.sm))

                LinearProgressIndicator(
                    progress = { state.progressFraction },
                    modifier = Modifier.fillMaxWidth(),
                    color = FastingAccent,
                )

                Spacer(Modifier.height(MaterialTheme.spacing.sm))

                Row {
                    ritmOutlinedButton(
                        text = "Завершить",
                        onClick = { viewModel.stopFasting(false) },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.padding(horizontal = MaterialTheme.spacing.xs))
                    ritmOutlinedButton(
                        text = "Отменить",
                        onClick = { viewModel.stopFasting(true) },
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Text(
                    text = "Нет активного голодания",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(MaterialTheme.spacing.sm))

                Row {
                    ritmTonalButton(
                        text = "Начать",
                        onClick = onStartClick,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.padding(horizontal = MaterialTheme.spacing.xs))
                    ritmOutlinedButton(
                        text = "История",
                        onClick = onHistoryClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
