# ARCHITECTURE.md — Техническая архитектура «Ритм»

---

## Стек

| Слой | Технология | Версия (target) |
|---|---|---|
| Язык | Kotlin | 2.x |
| UI | Jetpack Compose | BOM latest stable |
| Навигация | Navigation Compose | 2.7+ |
| DI | Hilt | 2.51+ |
| БД | Room | 2.6+ |
| Настройки | DataStore Preferences | 1.1+ |
| Async | Coroutines / Flow | — |
| Фоновые задачи | WorkManager | 2.9+ |
| Тесты | JUnit4, Turbine, Compose UI Tests, Room in-memory | — |

---

## Модульная структура

```
ritm-android/
├── app/                    ← Application class, MainActivity, NavGraph root
├── core-common/            ← Extensions, utils, base classes, date helpers
├── core-ui/                ← Theme, colors, typography, spacing, base Composables
├── core-database/          ← RitmDatabase, все Entity, все DAO, Migrations
├── feature-home/           ← Today screen, HomeAggregator, TodayState
├── feature-cycle/          ← Cycle calendar, day journal, prediction
├── feature-water/          ← Water log, daily goal, history
├── feature-fasting/        ← Session management, timer, history
├── feature-steps/          ← Daily steps, sensor ingestion, history
├── feature-habits/         ← Habit CRUD, check-in, streak, history
└── feature-settings/       ← Reminders, goals, preferences
```

### Зависимости между модулями

```
app → все feature-* и core-*
feature-* → core-common, core-ui, core-database
core-database → core-common
core-ui → (ничего из проекта)
core-common → (ничего из проекта)
```

**Правило:** feature-* модули НЕ зависят друг от друга напрямую.
Взаимодействие через `feature-home` (агрегатор) или через общие entities в `core-database`.

---

## Внутренняя структура feature-модуля

```
feature-X/
├── data/
│   ├── repository/         ← XRepository (impl)
│   └── datasource/         ← local (Room), если нужны доп. источники
├── domain/
│   ├── model/              ← domain models (не Room entities)
│   ├── repository/         ← XRepository (interface)
│   └── usecase/            ← GetX, CreateX, UpdateX, DeleteX
└── presentation/
    ├── XScreen.kt          ← Composable
    ├── XViewModel.kt
    └── XUiState.kt         ← sealed class / data class
```

---

## Дата-модель (Room Entities)

```kotlin
// core-database

@Entity data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: HabitType,        // POSITIVE | AVOID
    val frequency: String,      // DAILY | WEEKDAYS | CUSTOM (JSON)
    val reminderTime: String?,  // "HH:mm" или null
    val isArchived: Boolean = false,
    val createdAt: Long
)

@Entity data class HabitCheckEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String,           // "yyyy-MM-dd"
    val status: CheckStatus     // DONE | SKIPPED | FAILED
)

@Entity data class CyclePeriodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: String,      // "yyyy-MM-dd"
    val endDate: String?
)

@Entity data class CycleDayLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val flowIntensity: Int,     // 0-4
    val symptoms: String,       // JSON array
    val mood: Int,              // 0-4
    val notes: String?
)

@Entity data class WaterEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val type: WaterType         // WATER | TEA | COFFEE
)

@Entity data class FastingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planType: String,       // "16:8", "23:1", etc.
    val fastingHours: Int,
    val startDateTime: Long,
    val plannedEndDateTime: Long,
    val actualEndDateTime: Long?,
    val status: FastingStatus   // ACTIVE | COMPLETED | CANCELLED
)

@Entity data class StepDailyEntity(
    @PrimaryKey val date: String, // "yyyy-MM-dd"
    val steps: Int,
    val source: String            // SENSOR | MANUAL_BASELINE
)
```

---

## Ключевой агрегат: TodayState

```kotlin
// feature-home/domain/model/TodayState.kt
data class TodayState(
    val date: LocalDate,
    val cycleDay: Int?,
    val cyclePhase: CyclePhase?,
    val fastingStatus: FastingWidgetState,
    val waterProgress: WaterProgress,
    val habits: List<HabitItem>,
    val stepsProgress: StepsProgress
)
```

`HomeAggregator` — use case, который комбинирует Flow из четырёх репозиториев в один `TodayState`.

---

## Навигация

```kotlin
// app/NavGraph.kt
sealed class Screen(val route: String) {
    object Today : Screen("today")
    object Habits : Screen("habits")
    object HabitDetail : Screen("habit/{id}")
    object Cycle : Screen("cycle")
    object Settings : Screen("settings")
}
```

Bottom navigation bar: Today | Привычки | Цикл | (История) | Настройки

Fasting и Вода — через bottom sheet из Today, без отдельной вкладки в навигации.
Шаги — блок на Today + отдельный экран истории шагов без отдельной top-level вкладки.

---

## DataStore (Preferences)

Ключи в `core-common/PreferenceKeys.kt`:
- `water_daily_goal: Int` (стаканов, default: 8)
- `fasting_default_plan: String` ("16:8")
- `reminder_water_enabled: Boolean`
- `reminder_water_time: String` ("HH:mm")
- `reminder_habits_enabled: Boolean`
- `reminder_habits_time: String`
- `steps_daily_goal: Int` (опционально для дальнейшей полировки, default: 8000)

---

## Правила именования

| Тип | Суффикс | Пример |
|---|---|---|
| Room Entity | Entity | `HabitEntity` |
| Domain Model | — | `Habit` |
| DAO | Dao | `HabitDao` |
| Repository interface | Repository | `HabitRepository` |
| Repository impl | RepositoryImpl | `HabitRepositoryImpl` |
| Use case | — глагол | `GetTodayHabits`, `CreateHabit` |
| ViewModel | ViewModel | `HabitsViewModel` |
| UI State | UiState | `HabitsUiState` |
| Screen composable | Screen | `HabitsScreen` |

---

## Тестовая стратегия

- **Use cases:** JUnit4 + MockK (без Android)
- **DAO:** Room in-memory database
- **ViewModel:** Turbine для Flow
- **UI:** Compose UI Tests (smoke — основные сценарии)
- **Уведомления:** интеграционные тесты WorkManager

---

## Минимальная версия Android

- `minSdk = 26` (Android 8.0)
- `targetSdk = 35`
- `compileSdk = 35`
