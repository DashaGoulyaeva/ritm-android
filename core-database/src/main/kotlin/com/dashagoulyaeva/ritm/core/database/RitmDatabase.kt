package com.dashagoulyaeva.ritm.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dashagoulyaeva.ritm.core.database.dao.CycleDao
import com.dashagoulyaeva.ritm.core.database.dao.FastingDao
import com.dashagoulyaeva.ritm.core.database.dao.HabitDao
import com.dashagoulyaeva.ritm.core.database.dao.StepsDao
import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import com.dashagoulyaeva.ritm.core.database.entity.CycleDayLogEntity
import com.dashagoulyaeva.ritm.core.database.entity.CyclePeriodEntity
import com.dashagoulyaeva.ritm.core.database.entity.FastingSessionEntity
import com.dashagoulyaeva.ritm.core.database.entity.HabitCheckEntity
import com.dashagoulyaeva.ritm.core.database.entity.HabitEntity
import com.dashagoulyaeva.ritm.core.database.entity.StepDailyEntity
import com.dashagoulyaeva.ritm.core.database.entity.WaterEntryEntity

@Database(
    entities = [
        WaterEntryEntity::class,
        HabitEntity::class,
        HabitCheckEntity::class,
        FastingSessionEntity::class,
        CyclePeriodEntity::class,
        CycleDayLogEntity::class,
        StepDailyEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class RitmDatabase : RoomDatabase() {
    abstract fun waterDao(): WaterDao
    abstract fun habitDao(): HabitDao
    abstract fun fastingDao(): FastingDao
    abstract fun cycleDao(): CycleDao
    abstract fun stepsDao(): StepsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS habits (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL DEFAULT '',
                        iconEmoji TEXT NOT NULL DEFAULT '✅',
                        colorHex TEXT NOT NULL DEFAULT '#66BB6A',
                        targetDays TEXT NOT NULL DEFAULT '1111111',
                        reminderTime TEXT,
                        isArchived INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS habit_checks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        habitId INTEGER NOT NULL,
                        date TEXT NOT NULL,
                        completedAt INTEGER NOT NULL,
                        FOREIGN KEY (habitId) REFERENCES habits(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_habit_checks_habitId ON habit_checks(habitId)")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS fasting_sessions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        plan TEXT NOT NULL,
                        targetHours INTEGER NOT NULL,
                        startedAt INTEGER NOT NULL,
                        plannedEndAt INTEGER NOT NULL,
                        actualEndAt INTEGER,
                        status TEXT NOT NULL DEFAULT 'ACTIVE'
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS cycle_periods (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        startDate TEXT NOT NULL,
                        endDate TEXT
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS cycle_day_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date TEXT NOT NULL,
                        flow TEXT NOT NULL DEFAULT 'NONE',
                        mood TEXT NOT NULL DEFAULT 'UNKNOWN',
                        symptoms TEXT NOT NULL DEFAULT '',
                        note TEXT NOT NULL DEFAULT ''
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_cycle_day_logs_date ON cycle_day_logs(date)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS step_daily_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date TEXT NOT NULL,
                        stepCount INTEGER NOT NULL,
                        lastUpdatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_step_daily_logs_date ON step_daily_logs(date)")
            }
        }
    }
}
