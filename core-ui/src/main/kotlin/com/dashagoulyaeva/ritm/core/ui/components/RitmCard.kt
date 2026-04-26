package com.dashagoulyaeva.ritm.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dashagoulyaeva.ritm.core.ui.theme.InkOnLight

@Composable
fun ritmCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(width = 1.dp, color = InkOnLight.copy(alpha = 0.12f)),
        shape = MaterialTheme.shapes.large,
        content = content,
    )
}

@Composable
fun ritmSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ritmCard(
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = { content() },
            )
        },
    )
}

/**
 * Section card with a left accent bar — matches design v4 TodayCard style.
 * The [accentColor] paints a 4dp vertical stripe on the left edge.
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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                content = { content() },
            )
        }
    }
}
