package com.dashagoulyaeva.ritm.feature.fasting.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun fastingBottomSheet(
    onDismiss: () -> Unit,
    viewModel: FastingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md),
        ) {
            Text(
                text = "Начать голодание",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Выберите режим",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FastingPlan.entries
                    .filter { it != FastingPlan.CUSTOM }
                    .forEach { plan ->
                        FilterChip(
                            selected = state.selectedPlan == plan,
                            onClick = { viewModel.selectPlan(plan) },
                            label = { Text(text = plan.displayName) },
                            colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FastingAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = FastingAccent,
                                ),
                        )
                    }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

            state.selectedPlan?.let { plan ->
                Text(
                    text = "Голодание ${plan.displayName}: ${plan.hours} часов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            }

            ritmButton(
                text = "Начать",
                onClick = {
                    viewModel.startFasting()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
        }
    }
}
