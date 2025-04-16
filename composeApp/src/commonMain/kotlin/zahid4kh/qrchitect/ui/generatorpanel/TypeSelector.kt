package zahid4kh.qrchitect.ui.generatorpanel

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import zahid4kh.qrchitect.domain.QrCodeType

@Composable
fun QrCodeTypeSelector(
    onShowSelector: () -> Unit,
    onSelectorShown: (Boolean) -> Boolean,
    selectedType: QrCodeType,
    onTypeChanged: (QrCodeType) -> Unit,
    onContentChanged: (String) -> Unit,
){
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
        ) {
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
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onShowSelector() }
                    .padding(8.dp)
            ) {
                Text(
                    text = selectedType.getDisplayName(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    FeatherIcons.ArrowRight,
                    contentDescription = "Select",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if(onSelectorShown(true)) 90f else 0f)
                )
            }

            if (onSelectorShown(true)) {
                Spacer(modifier = Modifier.height(8.dp))
                QrCodeTypeSelector(
                    selectedType = selectedType,
                    onTypeSelected = {
                        onTypeChanged(it)
                        onShowSelector()
                        onContentChanged(it.getPlaceholderContent())
                    }
                )
            }
        }
    }
}