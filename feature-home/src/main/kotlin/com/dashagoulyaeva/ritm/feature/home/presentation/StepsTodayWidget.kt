package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dashagoulyaeva.ritm.core.ui.components.RitmRhythm
import com.dashagoulyaeva.ritm.core.ui.components.ritmBanner
import com.dashagoulyaeva.ritm.core.ui.components.ritmSectionCard
import com.dashagoulyaeva.ritm.core.ui.theme.StepsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@Composable
fun stepsTodayWidget(
    state: StepsUiState,
    onHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    ritmSectionCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            Text(
                text = "Шаги",
                style = MaterialTheme.typography.titleMedium,
            )

            when {
                state.isLoading -> CircularProgressIndicator()
                state.fallback != null -> stepsFallbackBanner(state.fallback!!)
                else -> {
                    Text(
                        text = "${state.todaySteps} / ${state.dailyGoal}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = StepsAccent,
                    )
                }
            }

            ritmBanner(
                title = "История шагов",
                subtitle = "Посмотреть движение по дням",
                rhythm = RitmRhythm.Steps,
                actionLabel = "Открыть",
                onActionClick = onHistoryClick,
            )
        }
    }
}

@Composable
private fun stepsFallbackBanner(fallback: StepsFallback) {
    val (title, subtitle) = when (fallback) {
        StepsFallback.PermissionDenied -> "Нет доступа" to "Разреши шагомер в настройках, и я снова начну считать."
        StepsFallback.SensorUnavailable -> "Шагомер недоступен" to "На этом устройстве шаги могут не считаться автоматически."
        StepsFallback.NoDataYet -> "Пока тихо" to "Данных за сегодня ещё нет. Если телефон был не с тобой — бывает."
    }

    ritmBanner(
        title = title,
        subtitle = subtitle,
        rhythm = RitmRhythm.Steps,
    )
}
