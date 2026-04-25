package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingsScreen(
    onWaterSettingsClick: () -> Unit,
    onFastingSettingsClick: () -> Unit = {},
    onHabitsSettingsClick: () -> Unit = {},
    onReminderSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Настройки") }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(MaterialTheme.spacing.md),
        ) {
            item {
                Text(
                    text = "Вода",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
                )
                settingsItem(title = "Настройки воды", subtitle = "Цель, напоминания", onClick = onWaterSettingsClick)
                HorizontalDivider()
            }
            item {
                Text(
                    text = "Голодание",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
                )
                settingsItem(title = "Настройки голодания", subtitle = "Режим по умолчанию", onClick = onFastingSettingsClick)
                HorizontalDivider()
            }
            item {
                Text(
                    text = "Привычки",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
                )
                settingsItem(title = "Настройки привычек", subtitle = "Архив привычек", onClick = onHabitsSettingsClick)
                HorizontalDivider()
            }
            item {
                Text(
                    text = "Напоминания",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
                )
                settingsItem(title = "Напоминания", subtitle = "Привычки, голодание", onClick = onReminderSettingsClick)
                HorizontalDivider()
            }
            item {
                Text(
                    text = "Приложение",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
                )
                settingsItem(title = "О приложении", subtitle = "Ритм v1.0", onClick = onAboutClick)
            }
        }
    }
}

@Composable
private fun settingsItem(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = MaterialTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
