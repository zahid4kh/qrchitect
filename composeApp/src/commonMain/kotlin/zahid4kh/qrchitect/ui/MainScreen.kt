package zahid4kh.qrchitect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.PlusSquare
import compose.icons.feathericons.Star
import org.koin.compose.koinInject
import zahid4kh.qrchitect.ui.generatorpanel.QrGeneratorPanel
import zahid4kh.qrchitect.ui.historypanel.QrHistoryPanel
import zahid4kh.qrchitect.ui.previewpanel.customization.QrCustomizationDialog
import zahid4kh.qrchitect.ui.previewpanel.QrPreviewPanel
import zahid4kh.qrchitect.ui.templatepanel.QrTemplatePanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Generator") },
                    icon = { Icon(FeatherIcons.PlusSquare, contentDescription = "Generator") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("History") },
                    icon = { Icon(FeatherIcons.Clock, contentDescription = "History") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Templates") },
                    icon = { Icon(FeatherIcons.Star, contentDescription = "Templates") }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    when (selectedTab) {
                        0 -> QrGeneratorPanel(
                            selectedType = state.selectedQrCodeType,
                            onTypeChanged = viewModel::updateQrCodeType,
                            content = state.qrContent,
                            onContentChanged = viewModel::updateQrContent,
                            onGenerate = viewModel::generateQrCode,
                            errorCorrectionLevel = state.errorCorrectionLevel,
                            onErrorCorrectionLevelChanged = viewModel::updateErrorCorrectionLevel,
                            foregroundColor = state.foregroundColor,
                            onForegroundColorChanged = viewModel::updateForegroundColor,
                            backgroundColor = state.backgroundColor,
                            onBackgroundColorChanged = viewModel::updateBackgroundColor
                        )
                        1 -> QrHistoryPanel(
                            qrCodes = state.savedQrCodes,
                            onQrCodeSelected = viewModel::selectQrCode,
                            onQrCodeDeleted = viewModel::deleteQrCode
                        )
                        2 -> QrTemplatePanel(
                            templates = state.templates,
                            onTemplateSelected = { },
                            onTemplateDeleted = { },
                            onCreateNewTemplate = { }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                QrPreviewPanel(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    qrImage = state.currentQrImage,
                    isGenerating = state.isGenerating,
                    onSaveImage = viewModel::saveCurrentQrCode,
                    onCustomizeQrCode = viewModel::showCustomizationDialog
                )

                if (state.showCustomizationDialog) {
                    QrCustomizationDialog(
                        onDismissRequest = viewModel::hideCustomizationDialog,
                        onApplyCustomization = viewModel::updateCustomization,
                        initialCustomization = state.currentCustomization
                    )
                }
            }
        }
    }
}