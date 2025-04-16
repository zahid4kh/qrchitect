package zahid4kh.qrchitect.ui.generatorpanel

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.draw.rotate
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

            QrCodeTypeSelector(
                onShowSelector = {showTypeSelector = !showTypeSelector},
                onSelectorShown = { showTypeSelector == it },
                selectedType = selectedType,
                onTypeChanged = onTypeChanged,
                onContentChanged = onContentChanged
            )

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
                        maxLines = 8,
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OptionsSelector(
                errorCorrectionLevel = errorCorrectionLevel,
                onErrorCorrectionLevelChanged = { onErrorCorrectionLevelChanged(it) },
                foregroundColor = foregroundColor,
                onForegroundClicked = {
                    editingForeground = true
                    showColorPicker = true
                },
                onBackgroundClicked = {
                    editingForeground = false
                    showColorPicker = true
                },
                onShowColorPicker = {showColorPicker},
                backgroundColor = backgroundColor,
                onBackgroundColorChanged = {onBackgroundColorChanged(it)},
                onForegroundColorChanged = { onForegroundColorChanged(it) },
                isEditingForeground = {editingForeground},
                collapseColorPicker = {showColorPicker = false},
            )

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