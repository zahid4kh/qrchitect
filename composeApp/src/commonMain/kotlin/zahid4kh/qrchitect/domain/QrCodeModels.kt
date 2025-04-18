package zahid4kh.qrchitect.domain

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

data class QrCode(
    val id: Long = 0,
    val content: String,
    val type: QrCodeType,
    val name: String,
    val createdAt: Instant,
    val foregroundColor: Color,
    val backgroundColor: Color,
    val errorCorrectionLevel: ErrorCorrectionLevel,
    val imageBytes: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QrCode

        if (id != other.id) return false
        if (content != other.content) return false
        if (type != other.type) return false
        if (name != other.name) return false
        if (createdAt != other.createdAt) return false
        if (foregroundColor != other.foregroundColor) return false
        if (backgroundColor != other.backgroundColor) return false
        if (errorCorrectionLevel != other.errorCorrectionLevel) return false
        if (imageBytes != null) {
            if (other.imageBytes == null) return false
            if (!imageBytes.contentEquals(other.imageBytes)) return false
        } else if (other.imageBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + foregroundColor.hashCode()
        result = 31 * result + backgroundColor.hashCode()
        result = 31 * result + errorCorrectionLevel.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class QrTemplate(
    val id: Long = 0,
    val name: String,
    val foregroundColor: Color,
    val backgroundColor: Color,
    val logoPath: String? = null,
    val createdAt: Instant,
    val lastUsed: Instant? = null,
    val previewImageBytes: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QrTemplate

        if (id != other.id) return false
        if (name != other.name) return false
        if (foregroundColor != other.foregroundColor) return false
        if (backgroundColor != other.backgroundColor) return false
        if (logoPath != other.logoPath) return false
        if (createdAt != other.createdAt) return false
        if (lastUsed != other.lastUsed) return false
        if (previewImageBytes != null) {
            if (other.previewImageBytes == null) return false
            if (!previewImageBytes.contentEquals(other.previewImageBytes)) return false
        } else if (other.previewImageBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + foregroundColor.hashCode()
        result = 31 * result + backgroundColor.hashCode()
        result = 31 * result + (logoPath?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (lastUsed?.hashCode() ?: 0)
        result = 31 * result + (previewImageBytes?.contentHashCode() ?: 0)
        return result
    }
}

enum class ErrorCorrectionLevel(val displayName: String, val zxingValue: String) {
    L("Low (7%)", "L"),
    M("Medium (15%)", "M"),
    Q("Quartile (25%)", "Q"),
    H("High (30%)", "H");
}