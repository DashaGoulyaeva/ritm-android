package com.dashagoulyaeva.ritm.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import com.dashagoulyaeva.ritm.core.database.entity.WaterEntryEntity

@Database(
    entities = [WaterEntryEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class RitmDatabase : RoomDatabase() {
    abstract fun waterDao(): WaterDao
}
