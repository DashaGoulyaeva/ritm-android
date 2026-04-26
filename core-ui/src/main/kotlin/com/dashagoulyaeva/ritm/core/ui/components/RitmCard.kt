package com.dashagoulyaeva.ritm.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk

/**
 * Wobbly card shape matching design v4 token:
 * borderRadius: '22px 7px 19px 11px/9px 19px 11px 21px'
 * Approximated with RoundedCornerShape(different per corner).
 */
val RitmCardShape = RoundedCornerShape(
    topStart = 18.dp,
    topEnd   = 6.dp,
    bottomEnd   = 16.dp,
    bottomStart = 10.dp,
)

/** Alternate wobbly shape for variety */
val RitmCardShapeAlt = RoundedCornerShape(
    topStart = 8.dp,
    topEnd   = 20.dp,
    bottomEnd   = 8.dp,
    bottomStart = 18.dp,
)

@Composable
fun ritmCard(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RitmCardShape,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(width = 1.2.dp, color = InkOnLight.copy(alpha = 0.14f)),
        content = content,
    )
}

@Composable
fun ritmSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ritmCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = { content() },
        )
    }
}

/**
 * Accent card — left colour stripe, matches design v4 TodayCard.
 */
@Composable
fun ritmAccentCard(
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ritmCard(modifier = modifier) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor),
            )
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                content = { content() },
            )
        }
    }
}

/**
 * Compact today-card — design v4 TodayCard:
 * accent stripe | TITLE (uppercase small) | value (bold display) | sub | [right] | chevron
 */
@Composable
fun ritmTodayCard(
    accentColor: Color,
    title: String,
    value: String,
    sub: String? = null,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    softColor: Color = Color.Unspecified,
    right: @Composable (RowScope.() -> Unit)? = null,
    bottom: @Composable (ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val bg = if (active && softColor != Color.Unspecified) softColor else MaterialTheme.colorScheme.surface
    val borderColor = if (active) accentColor.copy(alpha = 0.45f) else InkOnLight.copy(alpha = 0.14f)
    val borderWidth = if (active) 1.4.dp else 1.2.dp

    Card(
        modifier = modifier,
        shape = RitmCardShape,
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(width = borderWidth, color = borderColor),
        onClick = onClick ?: {},
        enabled = onClick != null,
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Left accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor),
            )
            // Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Kicker: TITLE uppercase small
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                    // Value: bold display
                    Text(
                        text = value,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (sub != null) {
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (bottom != null) {
                        Column(content = { bottom() })
                    }
                }
                if (right != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = { right() },
                    )
                }
                // Chevron
                Text(
                    text = "›",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}
