package zahid4kh.qrchitect.data

import java.sql.Connection

interface AppDatabase {
    fun qrCodeDao(): QrCodeDao
    fun templateDao(): TemplateDao

    companion object {
        const val DATABASE_NAME = "qrchitect_db"
    }
}

class AppDatabaseImpl(private val connection: Connection) : AppDatabase {
    private val qrCodeDaoInstance = QrCodeDaoImpl(connection)
    private val templateDaoInstance = TemplateDaoImpl(connection)

    override fun qrCodeDao(): QrCodeDao = qrCodeDaoInstance
    override fun templateDao(): TemplateDao = templateDaoInstance
}