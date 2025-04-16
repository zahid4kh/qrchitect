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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.ErrorCorrectionLevel

@Composable
fun OptionsSelector(
    errorCorrectionLevel: ErrorCorrectionLevel,
    onErrorCorrectionLevelChanged: (ErrorCorrectionLevel) -> Unit,
    foregroundColor: Color,
    onForegroundClicked: () -> Unit,
    onBackgroundClicked: () -> Unit,
    onShowColorPicker: (Boolean) -> Boolean,
    backgroundColor: Color,
    onBackgroundColorChanged: (Color) -> Unit,
    onForegroundColorChanged: (Color) -> Unit,
    isEditingForeground: (Boolean) -> Boolean,
    collapseColorPicker: () -> Unit
){
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Options",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Error Correction Level",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ErrorCorrectionLevel.entries.forEach { level ->
                    FilterChip(
                        selected = errorCorrectionLevel == level,
                        onClick = { onErrorCorrectionLevelChanged(level) },
                        label = { Text(level.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Colors",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Foreground:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(foregroundColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .clickable {
                            onForegroundClicked()
//                            editingForeground = true
//                            showColorPicker = true
                        }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Background:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .clickable {
                            onBackgroundClicked()
//                            editingForeground = false
//                            showColorPicker = true
                        }
                )
            }

            if (onShowColorPicker(true)) {
                Spacer(modifier = Modifier.height(8.dp))
                MaterialColorSelector(
                    initialColor = if (isEditingForeground(true)) foregroundColor else backgroundColor,
                    onColorSelected = {
                        if (isEditingForeground(true)) {
                            onForegroundColorChanged(it)
                        } else {
                            onBackgroundColorChanged(it)
                        }
                        collapseColorPicker()
                    },
                    onDismiss = { collapseColorPicker() }
                )
            }
        }
    }
}