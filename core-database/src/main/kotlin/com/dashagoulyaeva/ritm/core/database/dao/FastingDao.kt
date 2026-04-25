package com.dashagoulyaeva.ritm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dashagoulyaeva.ritm.core.database.entity.FastingSessionEntity
import com.dashagoulyaeva.ritm.core.database.entity.FastingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FastingDao {
    @Query("SELECT * FROM fasting_sessions WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveSession(): Flow<FastingSessionEntity?>

    @Query("SELECT * FROM fasting_sessions WHERE status = 'ACTIVE' LIMIT 1")
    suspend fun getActiveSessionOnce(): FastingSessionEntity?

    @Query("SELECT * FROM fasting_sessions ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<FastingSessionEntity>>

    @Query("SELECT * FROM fasting_sessions WHERE startedAt >= :fromMs ORDER BY startedAt DESC")
    fun getSessionsSince(fromMs: Long): Flow<List<FastingSessionEntity>>

    @Insert
    suspend fun insertSession(session: FastingSessionEntity): Long

    @Update
    suspend fun updateSession(session: FastingSessionEntity)

    @Query("UPDATE fasting_sessions SET status = :status, actualEndAt = :endAt WHERE id = :id")
    suspend fun endSession(
        id: Long,
        status: FastingStatus,
        endAt: Long,
    )
}
