package com.dashagoulyaeva.ritm.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dashagoulyaeva.ritm.core.ui.theme.CycleAccent
import com.dashagoulyaeva.ritm.core.ui.theme.CycleSoft
import com.dashagoulyaeva.ritm.core.ui.theme.FastingAccent
import com.dashagoulyaeva.ritm.core.ui.theme.FastingSoft
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.HabitsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.Primary
import com.dashagoulyaeva.ritm.core.ui.theme.PrimaryContainer
import com.dashagoulyaeva.ritm.core.ui.theme.StepsAccent
import com.dashagoulyaeva.ritm.core.ui.theme.StepsSoft
import com.dashagoulyaeva.ritm.core.ui.theme.WaterAccent
import com.dashagoulyaeva.ritm.core.ui.theme.WaterSoft
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

enum class RitmRhythm {
    Today,
    Cycle,
    Fasting,
    Water,
    Steps,
    Habits,
}

data class RitmRhythmColors(
    val accent: Color,
    val soft: Color,
    val onSoft: Color = InkOnLight,
)

fun RitmRhythm.colors(): RitmRhythmColors =
    when (this) {
        RitmRhythm.Today -> RitmRhythmColors(Primary, PrimaryContainer)
        RitmRhythm.Cycle -> RitmRhythmColors(CycleAccent, CycleSoft)
        RitmRhythm.Fasting -> RitmRhythmColors(FastingAccent, FastingSoft)
        RitmRhythm.Water -> RitmRhythmColors(WaterAccent, WaterSoft)
        RitmRhythm.Steps -> RitmRhythmColors(StepsAccent, StepsSoft)
        RitmRhythm.Habits -> RitmRhythmColors(HabitsAccent, HabitsSoft)
    }

@Composable
@Suppress("LongParameterList")
fun ritmBanner(
    title: String,
    subtitle: String,
    rhythm: RitmRhythm,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    val colors = rhythm.colors()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.soft,
        contentColor = colors.onSoft,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(colors.accent),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSoft.copy(alpha = 0.76f),
                )
            }
            if (actionLabel != null && onActionClick != null) {
                ritmOutlinedButton(
                    text = actionLabel,
                    onClick = onActionClick,
                )
            }
        }
    }
}

@Composable
fun rhythmOrb(
    label: String,
    value: String,
    rhythm: RitmRhythm,
    modifier: Modifier = Modifier,
    size: Dp = 84.dp,
) {
    val colors = rhythm.colors()

    Surface(
        modifier = modifier.size(size),
        color = colors.soft,
        contentColor = colors.onSoft,
        shape = CircleShape,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent,
                textAlign = TextAlign.Center,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = colors.onSoft.copy(alpha = 0.72f),
            )
        }
    }
}
