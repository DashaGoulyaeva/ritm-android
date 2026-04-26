package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmButton
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fastingSettingsScreen(
    onBack: () -> Unit,
    viewModel: FastingSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки голодания") },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
        ) {
            Text(
                text = "Режим по умолчанию",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm),
            )

            FastingPlan.entries.forEach { plan ->
                fastingPlanRow(
                    plan = plan,
                    selected = uiState.selectedPlan == plan,
                    onSelect = { viewModel.selectPlan(plan) },
                )
                HorizontalDivider()
            }

            if (uiState.saved) {
                Text(
                    text = "Сохранено ✓",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
                )
            }

            ritmButton(
                text = "Сохранить",
                onClick = { viewModel.savePlan() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.md),
            )
        }
    }
}

@Composable
private fun fastingPlanRow(
    plan: FastingPlan,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = plan.displayName, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = planDescription(plan),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun planDescription(plan: FastingPlan): String =
    when (plan) {
        FastingPlan.PLAN_16_8 -> "16 часов голодания, 8 часов еды — популярный старт"
        FastingPlan.PLAN_18_6 -> "18 часов голодания, 6 часов еды"
        FastingPlan.PLAN_20_4 -> "20 часов голодания, 4 часа еды"
        FastingPlan.PLAN_24 -> "24 часа — один раз в день"
        FastingPlan.CUSTOM -> "Задать своё окно при старте"
    }
