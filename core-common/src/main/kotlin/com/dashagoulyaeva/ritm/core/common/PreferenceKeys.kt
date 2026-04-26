package com.dashagoulyaeva.ritm.core.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val WATER_DAILY_GOAL = intPreferencesKey("water_daily_goal")
    val FASTING_DEFAULT_PLAN = stringPreferencesKey("fasting_default_plan")
    val REMINDER_WATER_ENABLED = booleanPreferencesKey("reminder_water_enabled")
    val REMINDER_WATER_TIME = stringPreferencesKey("reminder_water_time")
    val REMINDER_HABITS_ENABLED = booleanPreferencesKey("reminder_habits_enabled")
    val REMINDER_HABITS_TIME = stringPreferencesKey("reminder_habits_time")
    val REMINDER_FASTING_ENABLED = booleanPreferencesKey("reminder_fasting_enabled")
}
