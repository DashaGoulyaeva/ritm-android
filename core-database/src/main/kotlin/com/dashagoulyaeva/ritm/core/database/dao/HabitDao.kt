package com.dashagoulyaeva.ritm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dashagoulyaeva.ritm.core.database.entity.HabitCheckEntity
import com.dashagoulyaeva.ritm.core.database.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
@Suppress("TooManyFunctions")
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt ASC")
    fun getActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): HabitEntity?

    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("UPDATE habits SET isArchived = 1 WHERE id = :habitId")
    suspend fun archiveHabit(habitId: Long)

    @Query("SELECT * FROM habit_checks WHERE habitId = :habitId ORDER BY date DESC")
    fun getChecksForHabit(habitId: Long): Flow<List<HabitCheckEntity>>

    @Query("SELECT * FROM habit_checks WHERE date = :date")
    fun getChecksForDate(date: String): Flow<List<HabitCheckEntity>>

    @Query("SELECT * FROM habit_checks WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getCheckForHabitAndDate(
        habitId: Long,
        date: String,
    ): HabitCheckEntity?

    @Insert
    suspend fun insertCheck(check: HabitCheckEntity)

    @Query("DELETE FROM habit_checks WHERE habitId = :habitId AND date = :date")
    suspend fun deleteCheck(
        habitId: Long,
        date: String,
    )
}
