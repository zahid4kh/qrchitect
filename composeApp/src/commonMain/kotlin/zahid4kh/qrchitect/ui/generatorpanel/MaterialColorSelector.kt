package zahid4kh.qrchitect.ui.generatorpanel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MaterialColorSelector(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val materialColors = listOf(
        Color(0xFF6200EE), // Purple
        Color(0xFF3700B3), // Dark Purple
        Color(0xFF03DAC6), // Teal
        Color(0xFF018786), // Dark Teal
        Color(0xFFE53935), // Red
        Color(0xFFF44336), // Light Red
        Color(0xFF43A047), // Green
        Color(0xFF388E3C), // Dark Green
        Color(0xFF1E88E5), // Blue
        Color(0xFF1976D2), // Dark Blue
        Color(0xFFFDD835), // Yellow
        Color(0xFFFBC02D), // Dark Yellow
        Color(0xFFFF6D00), // Orange
        Color(0xFFF57C00), // Dark Orange
        Color(0xFF3E2723), // Brown
        Color(0xFF000000), // Black
        Color(0xFF757575), // Gray
        Color(0xFFFFFFFF), // White
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Color",
                style = MaterialTheme.typography.titleSmall
            )
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in materialColors.indices step 3) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (j in 0..2) {
                        val colorIndex = i + j
                        if (colorIndex < materialColors.size) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(materialColors[colorIndex])
                                    .border(
                                        width = if (materialColors[colorIndex] == initialColor) 3.dp else 1.dp,
                                        color = if (materialColors[colorIndex] == initialColor)
                                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onColorSelected(materialColors[colorIndex]) }
                            )
                        } else {
                            Spacer(modifier = Modifier.size(60.dp))
                        }
                    }
                }
            }
        }
    }
}