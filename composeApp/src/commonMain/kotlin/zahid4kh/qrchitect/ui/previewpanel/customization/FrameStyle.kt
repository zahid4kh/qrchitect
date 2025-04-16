package zahid4kh.qrchitect.ui.previewpanel.customization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.FrameStyle
import zahid4kh.qrchitect.domain.FrameType

@Composable
fun FrameStyle(
    frameStyle: FrameStyle? = null
){
    var frameStyle by remember { mutableStateOf(frameStyle) }

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
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}