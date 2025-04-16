package zahid4kh.qrchitect.data

import kotlinx.datetime.Instant
import zahid4kh.qrchitect.domain.QrCodeType

class QrCodeTypeConverter {
    fun fromQrCodeType(type: QrCodeType): String {
        return type.name
    }

    fun toQrCodeType(value: String): QrCodeType {
        return QrCodeType.valueOf(value)
    }
}

class InstantConverter {
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    fun toInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }
}