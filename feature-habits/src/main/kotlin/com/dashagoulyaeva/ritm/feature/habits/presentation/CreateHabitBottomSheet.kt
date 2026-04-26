package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.dashagoulyaeva.ritm.core.ui.components.RitmCardShape
import com.dashagoulyaeva.ritm.core.ui.theme.SpaceGrotesk
import com.dashagoulyaeva.ritm.core.ui.theme.spacing

private val HabitsAccent = Color(0xFF66BB6A)
private val InkOnLight = Color(0xFF0F1115)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createHabitBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (title: String, emoji: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var title by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("✅") }

    val suggestedEmojis = listOf("✅", "🏃", "💧", "🧘", "📚", "🚭", "😴", "🥗")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp)
                .padding(bottom = 80.dp),
        ) {
            // 1. Drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp, bottom = 12.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .background(
                        color = InkOnLight.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp),
                    ),
            )

            // 2. Title
            Text(
                text = "новая привычка",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = InkOnLight,
            )

            // 3. Section header "НАЗВАНИЕ"
            Text(
                text = "название".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 0.8.sp,
                color = InkOnLight.copy(alpha = 0.55f),
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
            )

            // 4. Name field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        text = "медитировать 10 минут",
                        color = InkOnLight.copy(alpha = 0.4f),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RitmCardShape,
            )

            // 5. Section header "ИКОНКА"
            Text(
                text = "иконка".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 0.8.sp,
                color = InkOnLight.copy(alpha = 0.55f),
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
            )

            // 6. Emoji row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                suggestedEmojis.forEach { e ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (e == emoji) HabitsAccent.copy(alpha = 0.2f)
                                else InkOnLight.copy(alpha = 0.06f),
                            )
                            .clickable { emoji = e },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = e,
                            fontSize = 18.sp,
                        )
                    }
                }
            }

            // 7. Spacer
            Spacer(modifier = Modifier.height(24.dp))

            // 8. Create button
            Button(
                onClick = { if (title.isNotBlank()) onConfirm(title.trim(), emoji) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HabitsAccent),
                enabled = title.isNotBlank(),
            ) {
                Text(
                    text = "создать",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }

            // 9. Cancel link
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "отмена",
                    color = InkOnLight.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                )
            }
        }
    }
}
