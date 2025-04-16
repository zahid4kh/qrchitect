package zahid4kh.qrchitect.data

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import zahid4kh.qrchitect.domain.QrCodeType

class QrCodeTypeConverter {
    @TypeConverter
    fun fromQrCodeType(type: QrCodeType): String {
        return type.name
    }

    @TypeConverter
    fun toQrCodeType(value: String): QrCodeType {
        return QrCodeType.valueOf(value)
    }
}

class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }
}