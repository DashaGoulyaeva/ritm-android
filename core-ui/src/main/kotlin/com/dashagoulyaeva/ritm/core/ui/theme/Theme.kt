package com.dashagoulyaeva.ritm.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val LightColorScheme =
    lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        secondary = Secondary,
        secondaryContainer = SecondaryContainer,
        tertiary = Tertiary,
        tertiaryContainer = TertiaryContainer,
        error = Error,
        background = Background,
        surface = Surface,
        onSurface = OnSurface,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline,
    )

data class RitmSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

val LocalRitmSpacing = staticCompositionLocalOf { RitmSpacing() }

val MaterialTheme.spacing: RitmSpacing
    @Composable get() = LocalRitmSpacing.current

@Composable
fun ritmTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalRitmSpacing provides RitmSpacing()) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = RitmTypography,
            content = content,
        )
    }
}
