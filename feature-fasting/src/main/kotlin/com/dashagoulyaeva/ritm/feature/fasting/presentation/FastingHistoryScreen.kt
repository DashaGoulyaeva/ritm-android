package com.dashagoulyaeva.ritm.feature.fasting.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val HOUR_IN_MILLIS = 3_600_000L
private const val MINUTE_IN_MILLIS = 60_000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fastingHistoryScreen(
    onBack: () -> Unit,
    viewModel: FastingHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История голодания") },
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
        if (uiState.sessions.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Нет завершённых сессий")
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = MaterialTheme.spacing.md),
            ) {
                items(uiState.sessions) { session ->
                    fastingSessionCard(session = session)
                }
            }
        }
    }
}

@Composable
private fun fastingSessionCard(session: FastingSession) {
    ritmCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.md)) {
            val duration =
                (session.actualEndAt ?: System.currentTimeMillis()) - session.startedAt
            val hours = duration / HOUR_IN_MILLIS
            val minutes = (duration % HOUR_IN_MILLIS) / MINUTE_IN_MILLIS

            val dateFormatted =
                SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(Date(session.startedAt))

            val statusColor =
                when (session.status) {
                    FastingStatus.COMPLETED -> FastingAccent
                    FastingStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    FastingStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                }

            val statusText =
                when (session.status) {
                    FastingStatus.COMPLETED -> "Завершено"
                    FastingStatus.CANCELLED -> "Отменено"
                    FastingStatus.ACTIVE -> "Активно"
                }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = session.plan.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                )
            }

            Text(
                text = dateFormatted,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = "Продолжительность: %02d:%02d".format(hours, minutes),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
