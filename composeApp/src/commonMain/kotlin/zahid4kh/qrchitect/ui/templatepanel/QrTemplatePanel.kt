package zahid4kh.qrchitect.ui.templatepanel

import compose.icons.FeatherIcons
import zahid4kh.qrchitect.domain.QrTemplate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.feathericons.Image
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Star
import compose.icons.feathericons.Trash2
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.skia.Image

@Composable
fun QrTemplatePanel(
    templates: List<QrTemplate>,
    onTemplateSelected: (QrTemplate) -> Unit,
    onTemplateDeleted: (QrTemplate) -> Unit,
    onCreateNewTemplate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "QR Code Templates",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = onCreateNewTemplate,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(FeatherIcons.Plus, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Template")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (templates.isEmpty()) {
                EmptyState(
                    onCreateNewTemplate
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(templates) { template ->
                        TemplateCard(
                            template = template,
                            onTemplateClick = { onTemplateSelected(template) },
                            onDeleteClick = { onTemplateDeleted(template) }
                        )
                    }
                }
            }
        }
    }
}