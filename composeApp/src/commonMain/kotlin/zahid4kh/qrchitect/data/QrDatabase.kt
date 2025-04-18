package zahid4kh.qrchitect.data

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class QrDatabase {
    private var connection: Connection? = null
    private val qrCodeDaoInstance by lazy { QrCodeDaoImpl(connection!!) }
    private val templateDaoInstance by lazy { TemplateDaoImpl(connection!!) }

    fun initialize(databaseName: String = DATABASE_NAME): QrDatabase {
        Class.forName("org.sqlite.JDBC")

        val dbFolder = File(System.getProperty("user.home"), ".qrchitect")
        if (!dbFolder.exists()) {
            dbFolder.mkdirs()
        }

        val dbFile = File(dbFolder, databaseName)
        val jdbcUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        connection = DriverManager.getConnection(jdbcUrl)
        createTablesIfNeeded()
        return this
    }

    private fun createTablesIfNeeded() {
        connection?.createStatement()?.use { statement ->
            statement.execute("""
                CREATE TABLE IF NOT EXISTS qr_codes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    content TEXT NOT NULL,
                    type TEXT NOT NULL,
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    foreground_color INTEGER NOT NULL,
                    background_color INTEGER NOT NULL,
                    error_correction_level TEXT NOT NULL,
                    customization TEXT,
                    image_data BLOB
                )
            """)

            statement.execute("""
                CREATE TABLE IF NOT EXISTS templates (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    foreground_color INTEGER NOT NULL,
                    background_color INTEGER NOT NULL,
                    dot_style TEXT NOT NULL,
                    eye_style TEXT NOT NULL,
                    frame_style TEXT,
                    logo_path TEXT,
                    created_at INTEGER NOT NULL,
                    last_used INTEGER,
                    preview_image_data BLOB
                )
            """)
        }
    }

    fun qrCodeDao(): QrCodeDao = qrCodeDaoInstance
    fun templateDao(): TemplateDao = templateDaoInstance

    fun close() {
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val DATABASE_NAME = "qrchitect.db"
    }
}