package zahid4kh.qrchitect.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import zahid4kh.qrchitect.data.QrRepository
import zahid4kh.qrchitect.domain.*
import zahid4kh.qrchitect.utils.FileUtils
import java.io.File

class MainViewModel(
    private val qrRepository: QrRepository,
    private val qrCodeService: QrCodeService
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _selectedQrCodeType = MutableStateFlow(QrCodeType.TEXT)
    private val _qrContent = MutableStateFlow("")
    private val _errorCorrectionLevel = MutableStateFlow(ErrorCorrectionLevel.M)
    private val _foregroundColor = MutableStateFlow(Color.Black)
    private val _backgroundColor = MutableStateFlow(Color.White)
    private val _currentQrImage = MutableStateFlow<ImageBitmap?>(null)
    private val _isGenerating = MutableStateFlow(false)
    private val _savedQrCodes = MutableStateFlow<List<QrCode>>(emptyList())
    private val _currentCustomization = MutableStateFlow<QrCustomization?>(null)
    private val _showCustomizationDialog = MutableStateFlow(false)
    private val _templates = MutableStateFlow<List<QrTemplate>>(emptyList())

    val state: StateFlow<MainState> = combine(
        _selectedQrCodeType,
        _qrContent,
        _errorCorrectionLevel,
        _foregroundColor,
        _backgroundColor,
        _currentQrImage,
        _isGenerating,
        _savedQrCodes,
        _currentCustomization,
        _showCustomizationDialog,
        _templates
    ) { args ->
        MainState(
            selectedQrCodeType = args[0] as QrCodeType,
            qrContent = args[1] as String,
            errorCorrectionLevel = args[2] as ErrorCorrectionLevel,
            foregroundColor = args[3] as Color,
            backgroundColor = args[4] as Color,
            currentQrImage = args[5] as? ImageBitmap,
            isGenerating = args[6] as Boolean,
            savedQrCodes = args[7] as List<QrCode>,
            currentCustomization = args[8] as? QrCustomization,
            showCustomizationDialog = args[9] as Boolean,
            templates = args[10] as List<QrTemplate>
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    init {
        scope.launch {
            qrRepository.getAllQrCodesAsFlow().collect { qrCodes ->
                _savedQrCodes.value = qrCodes
            }
        }

        scope.launch {
            qrRepository.getAllTemplatesAsFlow().collect { templates ->
                _templates.value = templates
            }
        }
    }

    fun updateQrCodeType(type: QrCodeType) {
        _selectedQrCodeType.value = type
        _qrContent.value = type.getPlaceholderContent()
    }

    fun updateQrContent(content: String) {
        _qrContent.value = content
    }

    fun updateErrorCorrectionLevel(level: ErrorCorrectionLevel) {
        _errorCorrectionLevel.value = level
    }

    fun updateForegroundColor(color: Color) {
        _foregroundColor.value = color
    }

    fun updateBackgroundColor(color: Color) {
        _backgroundColor.value = color
    }

    fun generateQrCode() {
        val content = _qrContent.value
        if (content.isBlank()) return

        scope.launch {
            _isGenerating.value = true

            try {
                val qrBitmap = qrCodeService.generateQrCode(
                    content = content,
                    foregroundColor = _foregroundColor.value,
                    backgroundColor = _backgroundColor.value,
                    errorCorrectionLevel = _errorCorrectionLevel.value,
                    customization = _currentCustomization.value
                )

                _currentQrImage.value = qrBitmap
            } catch (e: Exception) {
                println("Error generating QR code: ${e.message}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun saveCurrentQrCode() {
        val qrImage = _currentQrImage.value ?: return
        val content = _qrContent.value
        val type = _selectedQrCodeType.value

        scope.launch {
            try {
                val qrBytes = qrCodeService.generateQrCodeBytes(
                    content = content,
                    foregroundColor = _foregroundColor.value,
                    backgroundColor = _backgroundColor.value,
                    errorCorrectionLevel = _errorCorrectionLevel.value,
                    customization = _currentCustomization.value
                )

                val name = generateQrCodeName(type, content)

                val qrCode = QrCode(
                    content = content,
                    type = type,
                    name = name,
                    createdAt = Clock.System.now(),
                    foregroundColor = _foregroundColor.value,
                    backgroundColor = _backgroundColor.value,
                    errorCorrectionLevel = _errorCorrectionLevel.value,
                    imageBytes = qrBytes,
                    customization = _currentCustomization.value
                )

                qrRepository.saveQrCode(qrCode)

                saveQrCodeToFile(name)
            } catch (e: Exception) {
                println("Error saving QR code: ${e.message}")
            }
        }
    }

    fun selectQrCode(qrCode: QrCode) {
        _selectedQrCodeType.value = qrCode.type
        _qrContent.value = qrCode.content
        _foregroundColor.value = qrCode.foregroundColor
        _backgroundColor.value = qrCode.backgroundColor
        _errorCorrectionLevel.value = qrCode.errorCorrectionLevel

        qrCode.imageBytes?.let { bytes ->
            scope.launch {
                try {
                    val qrBitmap = qrCodeService.generateQrCode(
                        content = qrCode.content,
                        foregroundColor = qrCode.foregroundColor,
                        backgroundColor = qrCode.backgroundColor,
                        errorCorrectionLevel = qrCode.errorCorrectionLevel,
                        customization = qrCode.customization
                    )

                    _currentQrImage.value = qrBitmap
                } catch (e: Exception) {
                    println("Error loading QR code: ${e.message}")
                }
            }
        }
    }

    fun deleteQrCode(qrCode: QrCode) {
        scope.launch {
            qrRepository.deleteQrCode(qrCode)
        }
    }

    fun showCustomizationDialog() {
        _showCustomizationDialog.value = true
    }

    fun hideCustomizationDialog() {
        _showCustomizationDialog.value = false
    }

    fun updateCustomization(customization: QrCustomization) {
        _currentCustomization.value = customization
        generateQrCode()
    }

    private fun generateQrCodeName(type: QrCodeType, content: String): String {
        val typePrefix = type.name.lowercase().capitalize()
        val contentPreview = when (type) {
            QrCodeType.TEXT -> content.take(20).ifEmpty { "Empty" }
            QrCodeType.URL -> {
                val urlPattern = "(?:https?://)?(?:www\\.)?([\\w-]+(?:\\.[\\w-]+)+)".toRegex()
                val matchResult = urlPattern.find(content)
                matchResult?.groupValues?.getOrNull(1) ?: "website"
            }
            QrCodeType.EMAIL -> {
                val emailPattern = "\\b[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})\\b".toRegex()
                val matchResult = emailPattern.find(content)
                matchResult?.groupValues?.getOrNull(1) ?: "email"
            }
            else -> type.name.lowercase().capitalize()
        }

        return "$typePrefix - $contentPreview"
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { it.uppercase() }
    }

    private suspend fun saveQrCodeToFile(suggestedName: String) {
        val qrImage = _currentQrImage.value ?: return
        val sanitizedName = suggestedName.replace(Regex("[^a-zA-Z0-9.-]"), "_")

        try {
            FileUtils.saveImageToFile(qrImage, sanitizedName)
        } catch (e: Exception) {
            println("Error saving QR code to file: ${e.message}")
        }
    }
}

data class MainState(
    val selectedQrCodeType: QrCodeType = QrCodeType.TEXT,
    val qrContent: String = "",
    val errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val currentQrImage: ImageBitmap? = null,
    val isGenerating: Boolean = false,
    val savedQrCodes: List<QrCode> = emptyList(),
    val currentCustomization: QrCustomization? = null,
    val showCustomizationDialog: Boolean = false,
    val templates: List<QrTemplate> = emptyList()
)