package zahid4kh.qrchitect.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        QrCodeEntity::class,
        TemplateEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(QrCodeTypeConverter::class, InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QrCodeDao
    abstract fun templateDao(): TemplateDao

    companion object {
        const val DATABASE_NAME = "qrchitect_db"
    }
}