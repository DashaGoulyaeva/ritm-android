package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import java.time.format.DateTimeFormatter

@Composable
fun waterHistoryScreen(
    onBackClick: () -> Unit,
    viewModel: WaterHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ritmOutlinedButton(
            text = "Назад",
            onClick = onBackClick,
        )

        Text(
            text = "История воды",
            style = MaterialTheme.typography.headlineMedium,
        )

        if (uiState.isLoading) {
            CircularProgressIndicator()
            return@Column
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
            return@Column
        }

        if (uiState.days.isEmpty()) {
            Text(
                text = "Пока нет записей. Сделай первый лог воды на Today.",
                style = MaterialTheme.typography.bodyMedium,
            )
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(uiState.days) { day ->
                ritmSectionCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = day.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    day.entries.forEach { entry ->
                        Text(
                            text = "• ${entryTypeLabel(entry.type)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

private fun entryTypeLabel(type: WaterType): String {
    return when (type) {
        WaterType.WATER -> "Вода"
        WaterType.TEA -> "Чай"
        WaterType.COFFEE -> "Кофе"
    }
}
