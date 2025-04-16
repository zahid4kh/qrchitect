package zahid4kh.qrchitect.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.Instant
import zahid4kh.qrchitect.domain.QrCodeType

@Entity(tableName = "qr_codes")
@TypeConverters(QrCodeTypeConverter::class, InstantConverter::class)
data class QrCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val type: QrCodeType,
    val name: String,
    val createdAt: Instant,
    val foregroundColor: Int,
    val backgroundColor: Int,
    val errorCorrectionLevel: String,
    val customization: String? = null,
    val imageData: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QrCodeEntity

        if (id != other.id) return false
        if (content != other.content) return false
        if (type != other.type) return false
        if (name != other.name) return false
        if (createdAt != other.createdAt) return false
        if (foregroundColor != other.foregroundColor) return false
        if (backgroundColor != other.backgroundColor) return false
        if (errorCorrectionLevel != other.errorCorrectionLevel) return false
        if (customization != other.customization) return false
        if (imageData != null) {
            if (other.imageData == null) return false
            if (!imageData.contentEquals(other.imageData)) return false
        } else if (other.imageData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + foregroundColor
        result = 31 * result + backgroundColor
        result = 31 * result + errorCorrectionLevel.hashCode()
        result = 31 * result + (customization?.hashCode() ?: 0)
        result = 31 * result + (imageData?.contentHashCode() ?: 0)
        return result
    }
}