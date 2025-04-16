package zahid4kh.qrchitect.ui.previewpanel.customization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.DotStyle
import zahid4kh.qrchitect.domain.DotType

@Composable
fun DotStyle(
    dotStyle: DotStyle,
    defaultDotStyle: DotStyle
){
    var dotStyle by remember { mutableStateOf(dotStyle ?: defaultDotStyle) }


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
}