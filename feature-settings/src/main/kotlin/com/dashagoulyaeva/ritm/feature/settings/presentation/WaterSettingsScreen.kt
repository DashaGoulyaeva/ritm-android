package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@Composable
fun waterSettingsScreen(viewModel: WaterSettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Настройки воды",
            style = MaterialTheme.typography.headlineMedium,
        )

        if (uiState.isLoading) {
            CircularProgressIndicator()
            return@Column
        }

        goalSection(
            dailyGoal = uiState.dailyGoal,
            onDecreaseGoal = viewModel::decreaseGoal,
            onIncreaseGoal = viewModel::increaseGoal,
        )

        reminderSection(
            reminderEnabled = uiState.reminderEnabled,
            reminderTime = uiState.reminderTime,
            onToggleReminder = viewModel::toggleReminder,
            onUpdateReminderTime = viewModel::updateReminderTime,
            onSaveReminderTime = viewModel::saveReminderTime,
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun goalSection(
    dailyGoal: Int,
    onDecreaseGoal: () -> Unit,
    onIncreaseGoal: () -> Unit,
) {
    ritmSectionCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Text(
                text = "Дневная цель",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "$dailyGoal стаканов",
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                ritmOutlinedButton(
                    text = "-1",
                    onClick = onDecreaseGoal,
                )
                ritmButton(
                    text = "+1",
                    onClick = onIncreaseGoal,
                )
            }
        }
    }
}

@Composable
private fun reminderSection(
    reminderEnabled: Boolean,
    reminderTime: String,
    onToggleReminder: (Boolean) -> Unit,
    onUpdateReminderTime: (String) -> Unit,
    onSaveReminderTime: () -> Unit,
) {
    ritmSectionCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Напоминание о воде",
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = onToggleReminder,
                )
            }

            OutlinedTextField(
                value = reminderTime,
                onValueChange = onUpdateReminderTime,
                label = { Text("Время (HH:mm)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            ritmButton(
                text = "Сохранить время",
                onClick = onSaveReminderTime,
            )
        }
    }
}
