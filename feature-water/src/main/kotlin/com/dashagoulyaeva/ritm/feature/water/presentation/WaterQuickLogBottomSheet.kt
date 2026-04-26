package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.core.ui.components.RitmCardShape
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.WaterAccent
import com.dashagoulyaeva.ritm.core.ui.theme.WaterSoft

private val glassShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 4.dp,
    bottomStart = 10.dp,
    bottomEnd = 10.dp,
)

private fun drinkColor(type: WaterType): Color = when (type) {
    WaterType.WATER -> Color(0xFF29B6F6)
    WaterType.TEA -> Color(0xFFD98A3D)
    WaterType.COFFEE -> Color(0xFF3A2418)
}

private fun drinkEmoji(type: WaterType): String = when (type) {
    WaterType.WATER -> "💧"
    WaterType.TEA -> "🍵"
    WaterType.COFFEE -> "☕"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun waterQuickLogBottomSheet(
    uiState: WaterUiState,
    onDismissRequest: () -> Unit,
    onAddEntry: (WaterType) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        ) {
            // 1. DRAG HANDLE
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 14.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .background(
                        color = InkOnLight.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp),
                    ),
            )

            // 2. HEADER ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "ВОДА",
                        style = MaterialTheme.typography.labelSmall,
                        color = InkOnLight.copy(alpha = 0.55f),
                        letterSpacing = 0.8.sp,
                    )
                    Text(
                        text = "${uiState.todayCount} из ${uiState.dailyGoal}",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        color = InkOnLight,
                    )
                }
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = "история →",
                        style = MaterialTheme.typography.bodySmall,
                        color = WaterAccent,
                    )
                }
            }

            // 3. DRINK VISUAL SLOTS
            val slots = uiState.dailyGoal.coerceAtMost(8)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                repeat(slots) { i ->
                    val entry = uiState.todayEntries.getOrNull(i)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        if (entry != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height(38.dp)
                                        .clip(glassShape)
                                        .background(drinkColor(entry.type)),
                                )
                                Text(
                                    text = drinkEmoji(entry.type),
                                    fontSize = 10.sp,
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(38.dp)
                                    .clip(glassShape)
                                    .background(InkOnLight.copy(alpha = 0.08f))
                                    .border(
                                        width = 1.dp,
                                        color = InkOnLight.copy(alpha = 0.15f),
                                        shape = glassShape,
                                    ),
                            )
                        }
                    }
                }
            }

            // 4. DRINK LEGEND
            val waterCount = uiState.todayEntries.count { it.type == WaterType.WATER }
            val teaCount = uiState.todayEntries.count { it.type == WaterType.TEA }
            val coffeeCount = uiState.todayEntries.count { it.type == WaterType.COFFEE }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {
                if (waterCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(WaterAccent, RoundedCornerShape(2.dp)),
                        )
                        Text(text = "вода ×$waterCount", fontSize = 11.sp)
                    }
                }
                if (teaCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFFD98A3D), RoundedCornerShape(2.dp)),
                        )
                        Text(text = "чай ×$teaCount", fontSize = 11.sp)
                    }
                }
                if (coffeeCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF3A2418), RoundedCornerShape(2.dp)),
                        )
                        Text(text = "кофе ×$coffeeCount", fontSize = 11.sp)
                    }
                }
            }

            // 5. SECTION HEADER
            Text(
                text = "БЫСТРЫЙ ЛОГ",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 0.8.sp,
                color = InkOnLight.copy(alpha = 0.55f),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            )

            // 6. QUICK LOG GRID (3 columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // WATER card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RitmCardShape,
                    colors = CardDefaults.cardColors(containerColor = WaterSoft),
                    onClick = { onAddEntry(WaterType.WATER) },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = drinkEmoji(WaterType.WATER), fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+ вода",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                            color = InkOnLight,
                        )
                        Text(
                            text = "стакан",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkOnLight.copy(alpha = 0.55f),
                        )
                    }
                }

                // TEA card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RitmCardShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBEBD3)),
                    onClick = { onAddEntry(WaterType.TEA) },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = drinkEmoji(WaterType.TEA), fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+ чай",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                            color = InkOnLight,
                        )
                        Text(
                            text = "стакан",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkOnLight.copy(alpha = 0.55f),
                        )
                    }
                }

                // COFFEE card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RitmCardShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8D9C8)),
                    onClick = { onAddEntry(WaterType.COFFEE) },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = drinkEmoji(WaterType.COFFEE), fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+ кофе",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                            color = InkOnLight,
                        )
                        Text(
                            text = "стакан",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkOnLight.copy(alpha = 0.55f),
                        )
                    }
                }
            }

            // 7. UNDO LAST (if todayCount > 0)
            if (uiState.todayCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "отменить последнее",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkOnLight.copy(alpha = 0.5f),
                        )
                    }
                }
            }

            // 8. Error message
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
