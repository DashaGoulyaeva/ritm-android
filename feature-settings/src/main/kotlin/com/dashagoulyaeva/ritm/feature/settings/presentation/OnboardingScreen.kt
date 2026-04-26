package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashagoulyaeva.ritm.core.ui.components.ritmCard
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.CycleSoft
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.FastingSoft
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.StepsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.StepsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.WaterAccent
import com.dashagoulyaeva.ritm.core.ui.theme.WaterSoft

private data class RhythmItem(
    val color: Color,
    val soft: Color,
    val label: String,
    val why: String,
)

private val rhythmItems = listOf(
    RhythmItem(CycleAccent,   CycleSoft,   "цикл",      "28-дневный ритм тела"),
    RhythmItem(FastingAccent, FastingSoft, "голодание", "временны́е окна питания"),
    RhythmItem(WaterAccent,   WaterSoft,   "вода",      "гидратация в течение дня"),
    RhythmItem(StepsAccent,   StepsSoft,   "шаги",      "движение и активность"),
    RhythmItem(HabitsAccent,  HabitsSoft,  "привычки",  "делать и не-делать"),
)

private val HeroShape = RoundedCornerShape(
    topStart = 0.dp, topEnd = 0.dp,
    bottomStart = 24.dp, bottomEnd = 24.dp,
)

private val ButtonShape = RoundedCornerShape(
    topStart = 16.dp, topEnd = 6.dp,
    bottomStart = 10.dp, bottomEnd = 16.dp,
)

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun onboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableStateOf(0) }

    when (step) {
        0    -> OnboardingStep0(onNext = { step = 1 }, onSkip = onFinish)
        1    -> OnboardingStep1(onNext = { step = 2 }, onBack = { step = 0 })
        else -> OnboardingStep2(onFinish = onFinish)
    }
}

// ── Step 0 — Intro ────────────────────────────────────────────────────────────

@Composable
private fun OnboardingStep0(onNext: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Dark hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HeroShape)
                .background(Color(0xFF1C1B1F))
                .padding(horizontal = 18.dp, vertical = 32.dp),
        ) {
            Text(
                text = "1 / 3",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "один день.",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
            Text(
                text = "одно тело.",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
            Text(
                text = "пять ритмов.",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFFFE0A8),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "цикл · голодание · вода · шаги · привычки",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.55f),
            )
        }

        // Description
        Text(
            text = "ритм — не четыре приложения, а один экран дня. без облака, без аккаунтов. всё живёт на твоём телефоне.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkOnLight.copy(alpha = 0.7f),
            lineHeight = 22.sp,
            modifier = Modifier.padding(18.dp),
        )

        // Rhythm legend
        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            rhythmItems.forEach { item ->
                ritmCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(item.color)
                                .border(
                                    width = 1.2.dp,
                                    color = InkOnLight.copy(alpha = 0.14f),
                                    shape = CircleShape,
                                ),
                        )
                        Column {
                            Text(
                                text = item.label,
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleSmall,
                                color = InkOnLight,
                            )
                            Text(
                                text = item.why,
                                style = MaterialTheme.typography.bodySmall,
                                color = InkOnLight.copy(alpha = 0.6f),
                            )
                        }
                    }
                }
            }
        }

        // Bottom row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onSkip) {
                Text(
                    text = "пропустить",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkOnLight.copy(alpha = 0.45f),
                )
            }
            Button(
                onClick = onNext,
                shape = ButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = InkOnLight),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "дальше",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White,
                    )
                    Text(
                        text = "→",
                        fontSize = 15.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

// ── Step 1 — Permissions ──────────────────────────────────────────────────────

@Composable
private fun OnboardingStep1(onNext: () -> Unit, onBack: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { onNext() }

    Column(modifier = Modifier.fillMaxSize()) {
        // Dark hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HeroShape)
                .background(Color(0xFF1C1B1F))
                .padding(horizontal = 18.dp, vertical = 32.dp),
        ) {
            Text(
                text = "2 / 3",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "шаги",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "нужен доступ к активности",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f),
            )
        }

        // Explanation card
        ritmCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "что нужно",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    color = InkOnLight,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "для подсчёта шагов Android требует разрешение ACTIVITY_RECOGNITION",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkOnLight.copy(alpha = 0.65f),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            launcher.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)
                        },
                        shape = ButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = InkOnLight),
                    ) {
                        Text(
                            text = "разрешить",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    }
                    OutlinedButton(
                        onClick = onNext,
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Text(text = "пропустить", fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom nav
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "← назад",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkOnLight.copy(alpha = 0.55f),
                )
            }
            TextButton(onClick = onNext) {
                Text(
                    text = "пропустить →",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkOnLight.copy(alpha = 0.55f),
                )
            }
        }
    }
}

// ── Step 2 — Finish ───────────────────────────────────────────────────────────

@Composable
private fun OnboardingStep2(onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Dark hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HeroShape)
                .background(Color(0xFF1C1B1F))
                .padding(horizontal = 18.dp, vertical = 32.dp),
        ) {
            Text(
                text = "3 / 3",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "всё готово",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "начни отслеживать первый ритм",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f),
            )
        }

        Text(
            text = "данные хранятся только на этом устройстве. мы не знаем кто ты.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkOnLight.copy(alpha = 0.7f),
            modifier = Modifier.padding(18.dp),
        )

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = InkOnLight),
        ) {
            Text(
                text = "начать →",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
            )
        }
    }
}
