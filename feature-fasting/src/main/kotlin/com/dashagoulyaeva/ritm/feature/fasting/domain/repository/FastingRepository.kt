package com.dashagoulyaeva.ritm.feature.fasting.domain.repository

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import kotlinx.coroutines.flow.Flow

interface FastingRepository {
    fun getActiveSession(): Flow<FastingSession?>

    suspend fun getActiveSessionOnce(): FastingSession?

    fun getAllSessions(): Flow<List<FastingSession>>

    fun getSessionsSince(fromMs: Long): Flow<List<FastingSession>>

    suspend fun startSession(
        plan: FastingPlan,
        customHours: Int = 0,
    ): Long

    suspend fun stopSession(
        sessionId: Long,
        cancelled: Boolean = false,
    )
}
