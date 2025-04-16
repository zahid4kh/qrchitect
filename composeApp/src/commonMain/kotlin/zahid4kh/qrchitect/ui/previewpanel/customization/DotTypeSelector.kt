package zahid4kh.qrchitect.ui.previewpanel.customization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.DotType


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

private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}