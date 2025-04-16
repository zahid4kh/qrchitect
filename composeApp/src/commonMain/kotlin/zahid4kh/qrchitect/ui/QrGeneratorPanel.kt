package zahid4kh.qrchitect.ui

import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import zahid4kh.qrchitect.domain.QrCodeType
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.ErrorCorrectionLevel

@Composable
fun QrGeneratorPanel(
    selectedType: QrCodeType,
    onTypeChanged: (QrCodeType) -> Unit,
    content: String,
    onContentChanged: (String) -> Unit,
    onGenerate: () -> Unit,
    errorCorrectionLevel: ErrorCorrectionLevel,
    onErrorCorrectionLevelChanged: (ErrorCorrectionLevel) -> Unit,
    foregroundColor: Color,
    onForegroundColorChanged: (Color) -> Unit,
    backgroundColor: Color,
    onBackgroundColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTypeSelector by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var editingForeground by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "QR Code Generator",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "QR Code Type",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTypeSelector = true }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = selectedType.getDisplayName(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            FeatherIcons.ArrowRight,
                            contentDescription = "Select",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (showTypeSelector) {
                        Spacer(modifier = Modifier.height(8.dp))
                        QrCodeTypeSelector(
                            selectedType = selectedType,
                            onTypeSelected = {
                                onTypeChanged(it)
                                showTypeSelector = false
                                onContentChanged(it.getPlaceholderContent())
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Content",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = onContentChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter ${selectedType.getDisplayName()} content") },
                        minLines = 4,
                        maxLines = 8
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                                    editingForeground = true
                                    showColorPicker = true
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
                                    editingForeground = false
                                    showColorPicker = true
                                }
                        )
                    }

                    if (showColorPicker) {
                        Spacer(modifier = Modifier.height(8.dp))
                        MaterialColorSelector(
                            initialColor = if (editingForeground) foregroundColor else backgroundColor,
                            onColorSelected = {
                                if (editingForeground) {
                                    onForegroundColorChanged(it)
                                } else {
                                    onBackgroundColorChanged(it)
                                }
                                showColorPicker = false
                            },
                            onDismiss = { showColorPicker = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onGenerate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Generate QR Code",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun QrCodeTypeSelector(
    selectedType: QrCodeType,
    onTypeSelected: (QrCodeType) -> Unit
) {
    Column {
        QrCodeType.entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = type == selectedType,
                    onClick = { onTypeSelected(type) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = type.getDisplayName())
            }
        }
    }
}

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