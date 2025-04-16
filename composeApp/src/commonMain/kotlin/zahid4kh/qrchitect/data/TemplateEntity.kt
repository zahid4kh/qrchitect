package zahid4kh.qrchitect.data

import kotlinx.datetime.Instant

data class TemplateEntity(
    val id: Long = 0,
    val name: String,
    val foregroundColor: Int,
    val backgroundColor: Int,
    val dotStyle: String,
    val eyeStyle: String,
    val frameStyle: String? = null,
    val logoPath: String? = null,
    val createdAt: Instant,
    val lastUsed: Instant? = null,
    val previewImageData: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemplateEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (foregroundColor != other.foregroundColor) return false
        if (backgroundColor != other.backgroundColor) return false
        if (dotStyle != other.dotStyle) return false
        if (eyeStyle != other.eyeStyle) return false
        if (frameStyle != other.frameStyle) return false
        if (logoPath != other.logoPath) return false
        if (createdAt != other.createdAt) return false
        if (lastUsed != other.lastUsed) return false
        if (previewImageData != null) {
            if (other.previewImageData == null) return false
            if (!previewImageData.contentEquals(other.previewImageData)) return false
        } else if (other.previewImageData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + foregroundColor
        result = 31 * result + backgroundColor
        result = 31 * result + dotStyle.hashCode()
        result = 31 * result + eyeStyle.hashCode()
        result = 31 * result + (frameStyle?.hashCode() ?: 0)
        result = 31 * result + (logoPath?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (lastUsed?.hashCode() ?: 0)
        result = 31 * result + (previewImageData?.contentHashCode() ?: 0)
        return result
    }
}