package zahid4kh.qrchitect.ui.previewpanel.customization

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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

@Composable
fun LogoOptions(
    logoPath: String? = null
){

    var logoPath by remember { mutableStateOf(logoPath) }


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
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}