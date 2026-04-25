package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.components.ritmOutlinedButton
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createHabitBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (title: String, emoji: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var title by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("✅") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md)
                    .padding(bottom = MaterialTheme.spacing.xl),
        ) {
            Text(
                text = "Новая привычка",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название привычки") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))
            OutlinedTextField(
                value = emoji,
                onValueChange = { if (it.length <= 2) emoji = it },
                label = { Text("Эмодзи") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                ritmOutlinedButton(
                    text = "Отмена",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                ritmButton(
                    text = "Создать",
                    onClick = { if (title.isNotBlank()) onConfirm(title.trim(), emoji) },
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank(),
                )
            }
        }
    }
}
