# TASKS.md — Атомарные задачи MVP

> Статусы: TODO | IN_PROGRESS | DONE | BLOCKED
> Каждая задача = один вертикальный slice или инфраструктурная единица.
> Агент не берёт следующую задачу, пока не закрыта текущая.

---

## Этап -1: Lead Preflight

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| L-01 | Git bootstrap: инициализировать репозиторий и подключить origin | Lead | BLOCKED | — |
| L-02 | Android SDK wiring: настроить `sdk.dir`/`ANDROID_SDK_ROOT` и проверить сборку | Lead | DONE | — |
| L-03 | Проверить доступ к plugin-интеграциям `@github` и `@figma` | Lead | BLOCKED | — |

---

## Этап 0: Foundation

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| F-01 | Создать Android-проект с multi-module структурой (все модули как Gradle subprojects) | Builder | DONE | — |
| F-02 | Настроить Hilt: Application class, @HiltAndroidApp, базовые модули | Builder | DONE | F-01 |
| F-03 | Настроить Room: RitmDatabase, базовые конвертеры (LocalDate, enums) | Builder | DONE | F-01 |
| F-04 | Настроить DataStore: PreferenceKeys, базовый UserPreferencesRepository | Builder | DONE | F-01 |
| F-05 | core-ui: RitmTheme (цвета, типографика, spacing), базовые Composables (RitmCard, RitmButton, RitmScaffold) | Builder | DONE | F-01 |
| F-06 | Навигация: NavGraph, MainActivity, BottomNavBar (Today/Привычки/Цикл/Настройки) | Builder | DONE | F-02, F-05 |
| F-07 | Настроить lint, ktlint, detekt, базовые тест-конфигурации | Builder | DONE | F-01 |

---

## Этап 1: Вода (первый slice — самый простой)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| W-01 | WaterEntryEntity + WaterDao + Room migration | Builder | DONE | F-03 |
| W-02 | WaterRepository (interface + impl) + GetTodayWater + AddWaterEntry use cases | Builder | DONE | W-01 |
| W-03 | DataStore: water_daily_goal preference + GetWaterGoal + SetWaterGoal | Builder | DONE | F-04 |
| W-04 | WaterViewModel + WaterUiState | Builder | DONE | W-02, W-03 |
| W-05 | WaterBottomSheet — быстрый лог (3 кнопки: вода/чай/кофе) | Builder | DONE | W-04, F-05 |
| W-06 | WaterHistoryScreen — история по дням | Builder | DONE | W-04 |
| W-07 | Unit-тесты: WaterRepository, use cases | Test | DONE | W-02 |
| W-08 | WorkManager: WaterReminderWorker + настройка в Settings | Builder | DONE | W-02, F-06 |

---

## Этап 2: Привычки

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| H-01 | HabitEntity + HabitCheckEntity + HabitDao | Builder | TODO | F-03 |
| H-02 | HabitRepository + CreateHabit + ArchiveHabit + GetActiveHabits use cases | Builder | TODO | H-01 |
| H-03 | CheckHabitToday + GetHabitStreak + GetHabitHistory use cases | Builder | TODO | H-02 |
| H-04 | HabitsViewModel + HabitsUiState | Builder | TODO | H-02, H-03 |
| H-05 | HabitsScreen — список привычек + создание (bottom sheet) | Builder | TODO | H-04, F-05 |
| H-06 | HabitDetailScreen — история + стрик | Builder | TODO | H-04 |
| H-07 | Unit-тесты: streak calculation, check-in logic | Test | TODO | H-03 |
| H-08 | WorkManager: HabitReminderWorker (вечернее напоминание) | Builder | TODO | H-02 |

---

## Этап 3: Fasting

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| FA-01 | FastingSessionEntity + FastingDao | Builder | TODO | F-03 |
| FA-02 | FastingRepository + StartFasting + StopFasting + GetActiveSession use cases | Builder | TODO | FA-01 |
| FA-03 | GetFastingHistory + CalculateRemainingTime use cases | Builder | TODO | FA-02 |
| FA-04 | FastingViewModel + FastingUiState | Builder | TODO | FA-02, FA-03 |
| FA-05 | FastingBottomSheet — выбор режима + старт | Builder | TODO | FA-04, F-05 |
| FA-06 | FastingTimerWidget — активный таймер (для Today и отдельного экрана) | Builder | TODO | FA-04 |
| FA-07 | FastingHistoryScreen | Builder | TODO | FA-04 |
| FA-08 | Unit-тесты: time calculation, session state machine | Test | TODO | FA-02, FA-03 |
| FA-09 | WorkManager: FastingCompletionWorker (уведомление по окончании) | Builder | TODO | FA-02 |

---

## Этап 4: Цикл

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| C-01 | CyclePeriodEntity + CycleDayLogEntity + CycleDao | Builder | TODO | F-03 |
| C-02 | CycleRepository + LogPeriodStart + LogPeriodEnd + GetCurrentCycleDay use cases | Builder | TODO | C-01 |
| C-03 | PredictNextPeriod + GetCycleDayLog + SaveCycleDayLog use cases | Builder | TODO | C-02 |
| C-04 | CycleViewModel + CycleUiState | Builder | TODO | C-02, C-03 |
| C-05 | CycleCalendarScreen — календарь с отметками | Builder | TODO | C-04, F-05 |
| C-06 | CycleDayJournalScreen — дневник дня (выделения, симптомы, настроение, заметка) | Builder | TODO | C-04 |
| C-07 | Unit-тесты: cycle day calculation, period prediction | Test | TODO | C-02, C-03 |

---

## Этап 5: Интеграция (Today screen)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| T-01 | TodayState domain model + HomeAggregator use case | Builder | TODO | W-02, H-03, FA-02, C-02 |
| T-02 | HomeViewModel + flow combination | Builder | TODO | T-01 |
| T-03 | TodayScreen — сборка четырёх блоков | Builder | TODO | T-02, F-05 |
| T-04 | TodayScreen: CycleWidget (день цикла, фаза) | Builder | TODO | T-03 |
| T-05 | TodayScreen: WaterWidget (прогресс + быстрый лог) | Builder | TODO | T-03, W-05 |
| T-06 | TodayScreen: FastingWidget (статус/таймер) | Builder | TODO | T-03, FA-05 |
| T-07 | TodayScreen: HabitsWidget (список + отметка) | Builder | TODO | T-03 |
| T-08 | Empty states для каждого блока (нет данных) | Builder | TODO | T-03 |
| T-09 | UI smoke tests: Today screen flow | Test | TODO | T-03 |

---

## Этап 6: Настройки и полировка

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| S-01 | SettingsScreen — структура + навигация | Builder | TODO | F-06 |
| S-02 | Настройки воды (цель в стаканах, напоминание) | Builder | DONE | S-01, W-03, W-08 |
| S-03 | Настройки fasting (режим по умолчанию) | Builder | TODO | S-01, FA-02 |
| S-04 | Настройки привычек (список, архив) | Builder | TODO | S-01, H-02 |
| S-05 | Настройки напоминаний (вкл/выкл, время) | Builder | TODO | S-01 |
| S-06 | Onboarding (первый запуск — минимальный) | Builder | TODO | F-06 |

---

## Post-MVP backlog

*(Не реализовывать без явного решения Lead Agent)*

- Тёмная тема
- Экспорт данных
- Health Connect
- Виджеты главного экрана
- Аналитика и тренды
- Wear OS

