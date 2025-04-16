package zahid4kh.qrchitect.ui.previewpanel.customization

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import zahid4kh.qrchitect.domain.*

@Composable
fun QrCustomizationDialog(
    onDismissRequest: () -> Unit,
    onApplyCustomization: (QrCustomization) -> Unit,
    initialCustomization: QrCustomization? = null
) {
    val defaultDotStyle = DotStyle(type = DotType.SQUARE)
    val defaultEyeStyle = EyeStyle(type = EyeType.SQUARE)

    var dotStyle by remember { mutableStateOf(initialCustomization?.dotStyle ?: defaultDotStyle) }
    var eyeStyle by remember { mutableStateOf(initialCustomization?.eyeStyle ?: defaultEyeStyle) }
    var frameStyle by remember { mutableStateOf(initialCustomization?.frameStyle) }
    var logoPath by remember { mutableStateOf(initialCustomization?.logoPath) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Customize QR Code",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                DotStyle(
                    dotStyle = dotStyle,
                    defaultDotStyle = defaultDotStyle
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomizationSection(title = "Eye Style") {
                    EyeTypeSelector(
                        selectedType = eyeStyle.type,
                        onTypeSelected = {
                            eyeStyle = eyeStyle.copy(type = it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FrameStyle(
                    frameStyle = frameStyle
                )

                Spacer(modifier = Modifier.height(16.dp))

                LogoOptions(
                    logoPath = logoPath
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val customization = QrCustomization(
                                dotStyle = dotStyle,
                                eyeStyle = eyeStyle,
                                frameStyle = frameStyle,
                                logoPath = logoPath
                            )
                            onApplyCustomization(customization)
                            onDismissRequest()
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}