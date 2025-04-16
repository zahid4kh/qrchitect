package zahid4kh.qrchitect.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import zahid4kh.qrchitect.domain.QrCodeType
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

interface QrCodeDao {
    fun getAllQrCodesAsFlow(): Flow<List<QrCodeEntity>>
    suspend fun getQrCodeById(id: Long): QrCodeEntity?
    suspend fun getQrCodesByType(type: QrCodeType): List<QrCodeEntity>
    suspend fun getRecentQrCodes(limit: Int): List<QrCodeEntity>
    suspend fun insert(qrCode: QrCodeEntity): Long
    suspend fun update(qrCode: QrCodeEntity): Int
    suspend fun delete(qrCode: QrCodeEntity): Int
    suspend fun deleteQrCodeById(id: Long): Int
    suspend fun deleteAllQrCodes(): Int
}

class QrCodeDaoImpl(private val connection: Connection) : QrCodeDao {

    override fun getAllQrCodesAsFlow(): Flow<List<QrCodeEntity>> = flow {
        val qrCodes = withContext(Dispatchers.IO) {
            val result = mutableListOf<QrCodeEntity>()
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT * FROM qr_codes ORDER BY created_at DESC").use { resultSet ->
                    while (resultSet.next()) {
                        result.add(resultSet.toQrCodeEntity())
                    }
                }
            }
            result
        }
        emit(qrCodes)
    }.flowOn(Dispatchers.IO)

    override suspend fun getQrCodeById(id: Long): QrCodeEntity? = withContext(Dispatchers.IO) {
        connection.prepareStatement("SELECT * FROM qr_codes WHERE id = ?").use { statement ->
            statement.setLong(1, id)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) resultSet.toQrCodeEntity() else null
            }
        }
    }

    override suspend fun getQrCodesByType(type: QrCodeType): List<QrCodeEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<QrCodeEntity>()
        connection.prepareStatement("SELECT * FROM qr_codes WHERE type = ? ORDER BY created_at DESC").use { statement ->
            statement.setString(1, type.name)
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    result.add(resultSet.toQrCodeEntity())
                }
            }
        }
        result
    }

    override suspend fun getRecentQrCodes(limit: Int): List<QrCodeEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<QrCodeEntity>()
        connection.prepareStatement("SELECT * FROM qr_codes ORDER BY created_at DESC LIMIT ?").use { statement ->
            statement.setInt(1, limit)
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    result.add(resultSet.toQrCodeEntity())
                }
            }
        }
        result
    }

    override suspend fun insert(qrCode: QrCodeEntity): Long = withContext(Dispatchers.IO) {
        connection.prepareStatement("""
            INSERT INTO qr_codes (content, type, name, created_at, foreground_color, background_color, 
                                 error_correction_level, customization, image_data)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, Statement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setQrCodeEntityParams(qrCode)
            statement.executeUpdate()

            statement.generatedKeys.use { resultSet ->
                if (resultSet.next()) resultSet.getLong(1) else -1
            }
        }
    }

    override suspend fun update(qrCode: QrCodeEntity) = withContext(Dispatchers.IO) {
        connection.prepareStatement("""
            UPDATE qr_codes SET content = ?, type = ?, name = ?, created_at = ?, 
                               foreground_color = ?, background_color = ?, 
                               error_correction_level = ?, customization = ?, image_data = ?
            WHERE id = ?
        """).use { statement ->
            statement.setQrCodeEntityParams(qrCode)
            statement.setLong(10, qrCode.id)
            statement.executeUpdate()
        }
    }

    override suspend fun delete(qrCode: QrCodeEntity) = withContext(Dispatchers.IO) {
        deleteQrCodeById(qrCode.id)
    }

    override suspend fun deleteQrCodeById(id: Long) = withContext(Dispatchers.IO) {
        connection.prepareStatement("DELETE FROM qr_codes WHERE id = ?").use { statement ->
            statement.setLong(1, id)
            statement.executeUpdate()
        }
    }

    override suspend fun deleteAllQrCodes() = withContext(Dispatchers.IO) {
        connection.createStatement().use { statement ->
            statement.executeUpdate("DELETE FROM qr_codes")
        }
    }

    private fun PreparedStatement.setQrCodeEntityParams(qrCode: QrCodeEntity) {
        setString(1, qrCode.content)
        setString(2, qrCode.type.name)
        setString(3, qrCode.name)
        setLong(4, qrCode.createdAt.toEpochMilliseconds())
        setInt(5, qrCode.foregroundColor)
        setInt(6, qrCode.backgroundColor)
        setString(7, qrCode.errorCorrectionLevel)
        setString(8, qrCode.customization)
        setBytes(9, qrCode.imageData)
    }

    private fun ResultSet.toQrCodeEntity(): QrCodeEntity {
        val typeConverter = QrCodeTypeConverter()
        val instantConverter = InstantConverter()

        return QrCodeEntity(
            id = getLong("id"),
            content = getString("content"),
            type = typeConverter.toQrCodeType(getString("type")),
            name = getString("name"),
            createdAt = instantConverter.toInstant(getLong("created_at")),
            foregroundColor = getInt("foreground_color"),
            backgroundColor = getInt("background_color"),
            errorCorrectionLevel = getString("error_correction_level"),
            customization = getString("customization"),
            imageData = getBytes("image_data")
        )
    }
}