package zahid4kh.qrchitect.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import zahid4kh.qrchitect.domain.QrCodeType

@Dao
interface QrCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(qrCode: QrCodeEntity): Long

    @Update
    suspend fun update(qrCode: QrCodeEntity)

    @Delete
    suspend fun delete(qrCode: QrCodeEntity)

    @Query("SELECT * FROM qr_codes ORDER BY createdAt DESC")
    fun getAllQrCodesAsFlow(): Flow<List<QrCodeEntity>>

    @Query("SELECT * FROM qr_codes WHERE id = :id")
    suspend fun getQrCodeById(id: Long): QrCodeEntity?

    @Query("SELECT * FROM qr_codes WHERE type = :type ORDER BY createdAt DESC")
    suspend fun getQrCodesByType(type: QrCodeType): List<QrCodeEntity>

    @Query("SELECT * FROM qr_codes ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentQrCodes(limit: Int): List<QrCodeEntity>

    @Query("DELETE FROM qr_codes WHERE id = :id")
    suspend fun deleteQrCodeById(id: Long)

    @Query("DELETE FROM qr_codes")
    suspend fun deleteAllQrCodes()
}