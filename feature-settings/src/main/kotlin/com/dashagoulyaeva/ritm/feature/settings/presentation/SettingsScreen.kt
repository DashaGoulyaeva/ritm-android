package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

@Suppress("LongMethod", "MaxLineLength")
@Composable
fun settingsScreen(
    onWaterSettingsClick: () -> Unit,
    onFastingSettingsClick: () -> Unit = {},
    onHabitsSettingsClick: () -> Unit = {},
    onReminderSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // Hero banner
            item {
                val bannerShape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 8.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(bannerShape)
                        .background(Color(0xFF1C1B1F))
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                ) {
                    Column {
                        Text(
                            text = "ЕЩЁ".uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.55f),
                        )
                        Text(
                            text = "настройки",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.White,
                        )
                        Text(
                            text = "всё локально · без облака",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.65f),
                        )
                    }
                }
            }

            // Group: РИТМЫ
            item {
                settingsGroupHeader("РИТМЫ")
                ritmCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ) {
                    settingsRow(title = "цель по воде", subtitle = "8 стаканов", onClick = onWaterSettingsClick)
                    HorizontalDivider(
                        color = InkOnLight.copy(alpha = 0.08f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 14.dp),
                    )
                    settingsRow(title = "голодание по умолчанию", subtitle = "16:8", onClick = onFastingSettingsClick)
                    HorizontalDivider(
                        color = InkOnLight.copy(alpha = 0.08f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 14.dp),
                    )
                    settingsRow(title = "цель по шагам", subtitle = "10 000", onClick = null)
                }
            }

            // Group: НАПОМИНАНИЯ
            item {
                settingsGroupHeader("НАПОМИНАНИЯ")
                ritmCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ) {
                    settingsRow(title = "напоминания", subtitle = "привычки, голодание", onClick = onReminderSettingsClick)
                }
            }

            // Group: ПРИВЫЧКИ
            item {
                settingsGroupHeader("ПРИВЫЧКИ")
                ritmCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ) {
                    settingsRow(title = "управление привычками", subtitle = "архив привычек", onClick = onHabitsSettingsClick)
                }
            }

            // Group: ПРИВАТНОСТЬ
            item {
                settingsGroupHeader("ПРИВАТНОСТЬ")
                ritmCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ) {
                    settingsRow(title = "данные", subtitle = "на устройстве", onClick = null)
                    HorizontalDivider(
                        color = InkOnLight.copy(alpha = 0.08f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 14.dp),
                    )
                    settingsRow(title = "о приложении", subtitle = "ритм v1.0", onClick = onAboutClick)
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.padding(bottom = MaterialTheme.spacing.lg))
            }
        }
    }
}

@Composable
private fun settingsGroupHeader(name: String) {
    Text(
        text = name.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        letterSpacing = 0.8.sp,
        color = InkOnLight.copy(alpha = 0.45f),
        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 4.dp),
    )
}

@Composable
private fun settingsRow(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
) {
    val clickableModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = InkOnLight,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = InkOnLight.copy(alpha = 0.55f),
            )
        }
        if (onClick != null) {
            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = InkOnLight.copy(alpha = 0.4f),
            )
        }
    }
}
