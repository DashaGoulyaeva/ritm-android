package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun waterSettingsScreen(
    onBack: () -> Unit = {},
    viewModel: WaterSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки воды") },
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {

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
