package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dashagoulyaeva.ritm.core.database.entity.WaterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun waterQuickLogBottomSheet(
    uiState: WaterUiState,
    onDismissRequest: () -> Unit,
    onAddEntry: (WaterType) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Быстрый лог воды",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Сегодня: ${uiState.todayCount}/${uiState.dailyGoal} стаканов",
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                quickLogButton(
                    modifier = Modifier.weight(1f),
                    text = "Вода",
                    onClick = { onAddEntry(WaterType.WATER) },
                )
                quickLogButton(
                    modifier = Modifier.weight(1f),
                    text = "Чай",
                    onClick = { onAddEntry(WaterType.TEA) },
                )
                quickLogButton(
                    modifier = Modifier.weight(1f),
                    text = "Кофе",
                    onClick = { onAddEntry(WaterType.COFFEE) },
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            } ?: Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun quickLogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = text)
    }
}
