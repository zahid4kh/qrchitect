package zahid4kh.qrchitect.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: TemplateEntity): Long

    @Update
    suspend fun update(template: TemplateEntity)

    @Delete
    suspend fun delete(template: TemplateEntity)

    @Query("SELECT * FROM templates ORDER BY lastUsed DESC, createdAt DESC")
    fun getAllTemplatesAsFlow(): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM templates WHERE id = :id")
    suspend fun getTemplateById(id: Long): TemplateEntity?

    @Query("SELECT * FROM templates ORDER BY lastUsed DESC, createdAt DESC LIMIT :limit")
    suspend fun getRecentTemplates(limit: Int): List<TemplateEntity>

    @Query("UPDATE templates SET lastUsed = :lastUsed WHERE id = :id")
    suspend fun updateLastUsed(id: Long, lastUsed: Long)

    @Query("DELETE FROM templates WHERE id = :id")
    suspend fun deleteTemplateById(id: Long)

    @Query("DELETE FROM templates")
    suspend fun deleteAllTemplates()
}