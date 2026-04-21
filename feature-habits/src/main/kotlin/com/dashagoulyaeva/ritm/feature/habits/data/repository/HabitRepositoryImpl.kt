package com.dashagoulyaeva.ritm.feature.habits.data.repository

import com.dashagoulyaeva.ritm.core.database.dao.HabitDao
import com.dashagoulyaeva.ritm.core.database.entity.HabitCheckEntity
import com.dashagoulyaeva.ritm.core.database.entity.HabitEntity
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
) : HabitRepository {

    override fun getActiveHabits(): Flow<List<Habit>> =
        habitDao.getActiveHabits().map { list -> list.map { it.toDomain() } }

    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAllHabits().map { list -> list.map { it.toDomain() } }

    override suspend fun getHabitById(habitId: Long): Habit? =
        habitDao.getHabitById(habitId)?.toDomain()

    override suspend fun createHabit(habit: Habit): Long =
        habitDao.insertHabit(habit.toEntity())

    override suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(habit.toEntity())

    override suspend fun archiveHabit(habitId: Long) =
        habitDao.archiveHabit(habitId)

    override fun getChecksForHabit(habitId: Long): Flow<List<HabitCheck>> =
        habitDao.getChecksForHabit(habitId).map { list -> list.map { it.toDomain() } }

    override fun getChecksForDate(date: String): Flow<List<HabitCheck>> =
        habitDao.getChecksForDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun checkHabit(habitId: Long, date: String) {
        if (habitDao.getCheckForHabitAndDate(habitId, date) == null) {
            habitDao.insertCheck(HabitCheckEntity(habitId = habitId, date = date))
        }
    }

    override suspend fun uncheckHabit(habitId: Long, date: String) =
        habitDao.deleteCheck(habitId, date)

    override suspend fun isCheckedToday(habitId: Long, date: String): Boolean =
        habitDao.getCheckForHabitAndDate(habitId, date) != null

    private fun HabitEntity.toDomain() = Habit(
        id = id,
        title = title,
        description = description,
        iconEmoji = iconEmoji,
        colorHex = colorHex,
        targetDays = targetDays,
        reminderTime = reminderTime,
        isArchived = isArchived,
        createdAt = createdAt,
    )

    private fun Habit.toEntity() = HabitEntity(
        id = id,
        title = title,
        description = description,
        iconEmoji = iconEmoji,
        colorHex = colorHex,
        targetDays = targetDays,
        reminderTime = reminderTime,
        isArchived = isArchived,
        createdAt = createdAt,
    )

    private fun HabitCheckEntity.toDomain() = HabitCheck(
        id = id,
        habitId = habitId,
        date = date,
        completedAt = completedAt,
    )
}
