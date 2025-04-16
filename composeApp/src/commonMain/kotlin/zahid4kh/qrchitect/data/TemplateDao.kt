package zahid4kh.qrchitect.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

interface TemplateDao {
    fun getAllTemplatesAsFlow(): Flow<List<TemplateEntity>>
    suspend fun getTemplateById(id: Long): TemplateEntity?
    suspend fun getRecentTemplates(limit: Int): List<TemplateEntity>
    suspend fun insert(template: TemplateEntity): Long
    suspend fun update(template: TemplateEntity): Int
    suspend fun updateLastUsed(id: Long, lastUsed: Long): Int
    suspend fun delete(template: TemplateEntity): Int
    suspend fun deleteTemplateById(id: Long): Int
    suspend fun deleteAllTemplates(): Int
}

class TemplateDaoImpl(private val connection: Connection) : TemplateDao {

    override fun getAllTemplatesAsFlow(): Flow<List<TemplateEntity>> = flow {
        val templates = withContext(Dispatchers.IO) {
            val result = mutableListOf<TemplateEntity>()
            connection.createStatement().use { statement ->
                statement.executeQuery("""
                    SELECT * FROM templates 
                    ORDER BY last_used DESC, created_at DESC
                """).use { resultSet ->
                    while (resultSet.next()) {
                        result.add(resultSet.toTemplateEntity())
                    }
                }
            }
            result
        }
        emit(templates)
    }.flowOn(Dispatchers.IO)

    override suspend fun getTemplateById(id: Long): TemplateEntity? = withContext(Dispatchers.IO) {
        connection.prepareStatement("SELECT * FROM templates WHERE id = ?").use { statement ->
            statement.setLong(1, id)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) resultSet.toTemplateEntity() else null
            }
        }
    }

    override suspend fun getRecentTemplates(limit: Int): List<TemplateEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<TemplateEntity>()
        connection.prepareStatement("""
            SELECT * FROM templates 
            ORDER BY last_used DESC, created_at DESC 
            LIMIT ?
        """).use { statement ->
            statement.setInt(1, limit)
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    result.add(resultSet.toTemplateEntity())
                }
            }
        }
        result
    }

    override suspend fun insert(template: TemplateEntity): Long = withContext(Dispatchers.IO) {
        connection.prepareStatement("""
            INSERT INTO templates (name, foreground_color, background_color, dot_style, eye_style, 
                                  frame_style, logo_path, created_at, last_used, preview_image_data)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, Statement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setTemplateEntityParams(template)
            statement.executeUpdate()

            statement.generatedKeys.use { resultSet ->
                if (resultSet.next()) resultSet.getLong(1) else -1
            }
        }
    }

    override suspend fun update(template: TemplateEntity) = withContext(Dispatchers.IO) {
        connection.prepareStatement("""
            UPDATE templates 
            SET name = ?, foreground_color = ?, background_color = ?, dot_style = ?, 
                eye_style = ?, frame_style = ?, logo_path = ?, created_at = ?, 
                last_used = ?, preview_image_data = ?
            WHERE id = ?
        """).use { statement ->
            statement.setTemplateEntityParams(template)
            statement.setLong(11, template.id)
            statement.executeUpdate()
        }
    }

    override suspend fun updateLastUsed(id: Long, lastUsed: Long) = withContext(Dispatchers.IO) {
        connection.prepareStatement("UPDATE templates SET last_used = ? WHERE id = ?").use { statement ->
            statement.setLong(1, lastUsed)
            statement.setLong(2, id)
            statement.executeUpdate()
        }
    }

    override suspend fun delete(template: TemplateEntity) = withContext(Dispatchers.IO) {
        deleteTemplateById(template.id)
    }

    override suspend fun deleteTemplateById(id: Long) = withContext(Dispatchers.IO) {
        connection.prepareStatement("DELETE FROM templates WHERE id = ?").use { statement ->
            statement.setLong(1, id)
            statement.executeUpdate()
        }
    }

    override suspend fun deleteAllTemplates() = withContext(Dispatchers.IO) {
        connection.createStatement().use { statement ->
            statement.executeUpdate("DELETE FROM templates")
        }
    }

    private fun PreparedStatement.setTemplateEntityParams(template: TemplateEntity) {
        val instantConverter = InstantConverter()

        setString(1, template.name)
        setInt(2, template.foregroundColor)
        setInt(3, template.backgroundColor)
        setString(4, template.dotStyle)
        setString(5, template.eyeStyle)
        setString(6, template.frameStyle)
        setString(7, template.logoPath)
        setLong(8, instantConverter.fromInstant(template.createdAt))
        setObject(9, template.lastUsed?.let { instantConverter.fromInstant(it) })
        setBytes(10, template.previewImageData)
    }

    private fun ResultSet.toTemplateEntity(): TemplateEntity {
        val instantConverter = InstantConverter()
        val lastUsedValue = getLong("last_used")
        val lastUsed = if (wasNull()) null else instantConverter.toInstant(lastUsedValue)

        return TemplateEntity(
            id = getLong("id"),
            name = getString("name"),
            foregroundColor = getInt("foreground_color"),
            backgroundColor = getInt("background_color"),
            dotStyle = getString("dot_style"),
            eyeStyle = getString("eye_style"),
            frameStyle = getString("frame_style"),
            logoPath = getString("logo_path"),
            createdAt = instantConverter.toInstant(getLong("created_at")),
            lastUsed = lastUsed,
            previewImageData = getBytes("preview_image_data")
        )
    }
}