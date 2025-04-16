package zahid4kh.qrchitect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

                CustomizationSection(title = "Dot Style") {
                    Column {
                        DotTypeSelector(
                            selectedType = dotStyle.type,
                            onTypeSelected = {
                                dotStyle = dotStyle.copy(type = it)
                            }
                        )

                        if (dotStyle.type == DotType.ROUNDED) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Corner Radius")
                            Slider(
                                value = dotStyle.cornerRadius,
                                onValueChange = {
                                    dotStyle = dotStyle.copy(cornerRadius = it)
                                },
                                valueRange = 0f..0.5f
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Inset Percentage")
                        Slider(
                            value = dotStyle.insetPercentage,
                            onValueChange = {
                                dotStyle = dotStyle.copy(insetPercentage = it)
                            },
                            valueRange = 0f..0.25f
                        )
                    }
                }

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

                CustomizationSection(title = "Frame Style") {
                    var showFrameOptions by remember { mutableStateOf(frameStyle != null) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = showFrameOptions,
                            onCheckedChange = { checked ->
                                showFrameOptions = checked
                                if (!checked) {
                                    frameStyle = null
                                } else if (frameStyle == null) {
                                    frameStyle = FrameStyle(
                                        type = FrameType.SQUARE,
                                        width = 4f,
                                        padding = 16f
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Frame")
                    }

                    if (showFrameOptions) {
                        var tempFrameStyle = frameStyle ?: FrameStyle(
                            type = FrameType.SQUARE,
                            width = 4f,
                            padding = 16f
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Frame Type")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FrameType.entries.filter { it != FrameType.NONE }.forEach { type ->
                                FilterChip(
                                    selected = tempFrameStyle.type == type,
                                    onClick = {
                                        tempFrameStyle = tempFrameStyle.copy(type = type)
                                        frameStyle = tempFrameStyle
                                    },
                                    label = { Text(type.name.lowercase().capitalize()) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Frame Width")
                        Slider(
                            value = tempFrameStyle.width,
                            onValueChange = {
                                tempFrameStyle = tempFrameStyle.copy(width = it)
                                frameStyle = tempFrameStyle
                            },
                            valueRange = 1f..10f
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Frame Padding")
                        Slider(
                            value = tempFrameStyle.padding,
                            onValueChange = {
                                tempFrameStyle = tempFrameStyle.copy(padding = it)
                                frameStyle = tempFrameStyle
                            },
                            valueRange = 0f..50f
                        )

                        if (tempFrameStyle.type == FrameType.ROUNDED) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Corner Radius")
                            Slider(
                                value = tempFrameStyle.cornerRadius,
                                onValueChange = {
                                    tempFrameStyle = tempFrameStyle.copy(cornerRadius = it)
                                    frameStyle = tempFrameStyle
                                },
                                valueRange = 0f..0.5f
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomizationSection(title = "Logo Options") {
                    var showLogoOptions by remember { mutableStateOf(logoPath != null) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = showLogoOptions,
                            onCheckedChange = { checked ->
                                showLogoOptions = checked
                                if (!checked) {
                                    logoPath = null
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Logo")
                    }

                    if (showLogoOptions) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            // will put file chooser here
                        }) {
                            Text("Select Logo Image")
                        }

                        if (logoPath != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = logoPath!!.split("/").last(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }


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

@Composable
fun CustomizationSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun DotTypeSelector(
    selectedType: DotType,
    onTypeSelected: (DotType) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DotType.entries.forEach { type ->
                val isSelected = type == selectedType
                FilterChip(
                    selected = isSelected,
                    onClick = { onTypeSelected(type) },
                    label = { Text(type.name.lowercase().capitalize()) }
                )
            }
        }
    }
}

@Composable
fun EyeTypeSelector(
    selectedType: EyeType,
    onTypeSelected: (EyeType) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EyeType.entries.forEach { type ->
                val isSelected = type == selectedType
                FilterChip(
                    selected = isSelected,
                    onClick = { onTypeSelected(type) },
                    label = { Text(type.name.lowercase().capitalize()) }
                )
            }
        }
    }
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}