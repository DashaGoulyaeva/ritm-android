package com.dashagoulyaeva.ritm.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.feature.water.presentation.WaterViewModel
import com.dashagoulyaeva.ritm.feature.water.presentation.waterQuickLogBottomSheet
import com.dashagoulyaeva.ritm.feature.water.presentation.waterTodayWidget

@Composable
fun todayScreen(
    onOpenWaterHistory: () -> Unit,
    viewModel: WaterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var isQuickLogVisible by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineMedium,
        )

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        waterTodayWidget(
            uiState = uiState,
            onQuickLogClick = {
                viewModel.clearError()
                isQuickLogVisible = true
            },
            onHistoryClick = onOpenWaterHistory,
        )

        Text(
            text = "Модули Привычки / Цикл / Fasting подключаются следующими slice.",
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    if (isQuickLogVisible) {
        waterQuickLogBottomSheet(
            uiState = uiState,
            onDismissRequest = { isQuickLogVisible = false },
            onAddEntry = { type ->
                viewModel.addEntry(type)
                isQuickLogVisible = false
            },
        )
    }
}
