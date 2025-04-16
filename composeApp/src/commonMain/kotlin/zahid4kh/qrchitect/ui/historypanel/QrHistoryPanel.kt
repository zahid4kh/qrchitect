package zahid4kh.qrchitect.ui.historypanel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zahid4kh.qrchitect.domain.QrCode


@Composable
fun QrHistoryPanel(
    qrCodes: List<QrCode>,
    onQrCodeSelected: (QrCode) -> Unit,
    onQrCodeDeleted: (QrCode) -> Unit,
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
            Text(
                text = "QR Code History",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (qrCodes.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(qrCodes) { qrCode ->
                        QrHistoryItem(
                            qrCode = qrCode,
                            onItemClick = { onQrCodeSelected(qrCode) },
                            onDeleteClick = { onQrCodeDeleted(qrCode) }
                        )
                    }
                }
            }
        }
    }
}