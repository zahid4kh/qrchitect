package zahid4kh.qrchitect.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import zahid4kh.qrchitect.domain.*

class QrRepository(
    private val database: QrDatabase,
    private val json: Json
) {
    private val qrCodeDao = database.qrCodeDao()
    private val templateDao = database.templateDao()

    fun getAllQrCodesAsFlow(): Flow<List<QrCode>> {
        return qrCodeDao.getAllQrCodesAsFlow().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getQrCodeById(id: Long): QrCode? {
        return qrCodeDao.getQrCodeById(id)?.toDomainModel()
    }

    suspend fun getRecentQrCodes(limit: Int): List<QrCode> {
        return qrCodeDao.getRecentQrCodes(limit).map { it.toDomainModel() }
    }

    suspend fun getQrCodesByType(type: QrCodeType): List<QrCode> {
        return qrCodeDao.getQrCodesByType(type).map { it.toDomainModel() }
    }

    suspend fun saveQrCode(qrCode: QrCode): Long {
        val entity = qrCode.toEntity()
        return qrCodeDao.insert(entity)
    }

    suspend fun updateQrCode(qrCode: QrCode) {
        val entity = qrCode.toEntity()
        qrCodeDao.update(entity)
    }

    suspend fun deleteQrCode(qrCode: QrCode) {
        qrCodeDao.deleteQrCodeById(qrCode.id)
    }

    suspend fun deleteAllQrCodes() {
        qrCodeDao.deleteAllQrCodes()
    }

    fun getAllTemplatesAsFlow(): Flow<List<QrTemplate>> {
        return templateDao.getAllTemplatesAsFlow().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getTemplateById(id: Long): QrTemplate? {
        return templateDao.getTemplateById(id)?.toDomainModel()
    }

    suspend fun getRecentTemplates(limit: Int): List<QrTemplate> {
        return templateDao.getRecentTemplates(limit).map { it.toDomainModel() }
    }

    suspend fun saveTemplate(template: QrTemplate): Long {
        val entity = template.toEntity()
        return templateDao.insert(entity)
    }

    suspend fun updateTemplate(template: QrTemplate) {
        val entity = template.toEntity()
        templateDao.update(entity)
    }

    suspend fun updateTemplateUsage(id: Long) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        templateDao.updateLastUsed(id, currentTime)
    }

    suspend fun deleteTemplate(template: QrTemplate) {
        templateDao.deleteTemplateById(template.id)
    }

    suspend fun deleteAllTemplates() {
        templateDao.deleteAllTemplates()
    }

    private fun QrCodeEntity.toDomainModel(): QrCode {
        return QrCode(
            id = id,
            content = content,
            type = type,
            name = name,
            createdAt = createdAt,
            foregroundColor = Color(foregroundColor),
            backgroundColor = Color(backgroundColor),
            errorCorrectionLevel = ErrorCorrectionLevel.valueOf(errorCorrectionLevel),
            imageBytes = imageData
        )
    }

    private fun QrCode.toEntity(): QrCodeEntity {

        return QrCodeEntity(
            id = id,
            content = content,
            type = type,
            name = name,
            createdAt = createdAt,
            foregroundColor = foregroundColor.toArgb(),
            backgroundColor = backgroundColor.toArgb(),
            errorCorrectionLevel = errorCorrectionLevel.name,
            imageData = imageBytes
        )
    }

    private fun TemplateEntity.toDomainModel(): QrTemplate {
        return QrTemplate(
            id = id,
            name = name,
            foregroundColor = Color(foregroundColor),
            backgroundColor = Color(backgroundColor),
            logoPath = logoPath,
            createdAt = createdAt,
            lastUsed = lastUsed,
            previewImageBytes = previewImageData
        )
    }

    private fun QrTemplate.toEntity(): TemplateEntity {
        return TemplateEntity(
            id = id,
            name = name,
            foregroundColor = foregroundColor.toArgb(),
            backgroundColor = backgroundColor.toArgb(),
            logoPath = logoPath,
            createdAt = createdAt,
            lastUsed = lastUsed,
            previewImageData = previewImageBytes
        )
    }
}