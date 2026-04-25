# TASKS.md — Атомарные задачи MVP

> Статусы: TODO | IN_PROGRESS | DONE | BLOCKED
> Каждая задача = один вертикальный slice или инфраструктурная единица.
> Следующая задача берётся только после закрытия текущей.

---

## Этап -1: Lead Preflight

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| L-01 | Git bootstrap: инициализировать репозиторий и подключить origin | Lead | DONE | — |
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
| H-01 | HabitEntity + HabitCheckEntity + HabitDao | Builder | DONE | F-03 |
| H-02 | HabitRepository + CreateHabit + ArchiveHabit + GetActiveHabits use cases | Builder | DONE | H-01 |
| H-03 | CheckHabitToday + GetHabitStreak + GetHabitHistory use cases | Builder | DONE | H-02 |
| H-04 | HabitsViewModel + HabitsUiState | Builder | DONE | H-02, H-03 |
| H-05 | HabitsScreen — список привычек + создание (bottom sheet) | Builder | DONE | H-04, F-05 |
| H-06 | HabitDetailScreen — история + стрик | Builder | DONE | H-04 |
| H-07 | Unit-тесты: streak calculation, check-in logic | Test | DONE | H-03 |
| H-08 | WorkManager: HabitReminderWorker (вечернее напоминание) | Builder | DONE | H-02 |

---

## Этап 3: Fasting

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| FA-01 | FastingSessionEntity + FastingDao | Builder | DONE | F-03 |
| FA-02 | FastingRepository + StartFasting + StopFasting + GetActiveSession use cases | Builder | DONE | FA-01 |
| FA-03 | GetFastingHistory + CalculateRemainingTime use cases | Builder | DONE | FA-02 |
| FA-04 | FastingViewModel + FastingUiState | Builder | DONE | FA-02, FA-03 |
| FA-05 | FastingBottomSheet — выбор режима + старт | Builder | DONE | FA-04, F-05 |
| FA-06 | FastingTimerWidget — активный таймер (для Today и отдельного экрана) | Builder | DONE | FA-04 |
| FA-07 | FastingHistoryScreen | Builder | DONE | FA-04 |
| FA-08 | Unit-тесты: time calculation, session state machine | Test | DONE | FA-02, FA-03 |
| FA-09 | WorkManager: FastingCompletionWorker (уведомление по окончании) | Builder | DONE | FA-02 |

---

## Этап 4: Цикл

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| C-01 | CyclePeriodEntity + CycleDayLogEntity + CycleDao | Builder | DONE | F-03 |
| C-02 | CycleRepository + LogPeriodStart + LogPeriodEnd + GetCurrentCycleDay use cases | Builder | DONE | C-01 |
| C-03 | PredictNextPeriod + GetCycleDayLog + SaveCycleDayLog use cases | Builder | DONE | C-02 |
| C-04 | CycleViewModel + CycleUiState | Builder | DONE | C-02, C-03 |
| C-05 | CycleCalendarScreen — календарь с отметками | Builder | DONE | C-04, F-05 |
| C-06 | CycleDayJournalScreen — дневник дня (выделения, симптомы, настроение, заметка) | Builder | DONE | C-04 |
| C-07 | Unit-тесты: cycle day calculation, period prediction | Test | DONE | C-02, C-03 |

---

## Этап 4.5: Шаги (MVP без внешних синхронизаций)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| P-ST-01 | Mini-spec: шаги (Today + история + edge cases) | Product | TODO | — |
| A-ST-01 | Technical note/ADR: contracts для шагов | Architect | TODO | P-ST-01 |
| ST-01 | StepDailyEntity + StepsDao + Room migration | Builder | DONE | F-03, A-ST-01 |
| ST-02 | StepsSensorDataSource + permission check (`ACTIVITY_RECOGNITION`) | Builder | TODO | A-ST-01 |
| ST-03 | StepsRepository (sensor + local day baseline/cache) | Builder | TODO | ST-01, ST-02 |
| ST-04 | Use cases: ObserveTodaySteps + GetStepsHistory | Builder | TODO | ST-03 |
| ST-05 | StepsViewModel + StepsUiState | Builder | TODO | ST-04 |
| ST-06 | StepsTodayWidget (интеграция на Today) | Builder | TODO | ST-05, F-05 |
| ST-07 | StepsHistoryScreen + navigation route | Builder | TODO | ST-05, F-06 |
| T-ST-01 | Unit/DAO/UI smoke tests + edge cases по шагам | Test | TODO | ST-06, ST-07 |

---

## Этап 5: Интеграция (Today screen)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| T-01 | TodayState domain model + HomeAggregator use case | Builder | DONE | W-02, H-03, FA-02, C-02, ST-04 |
| T-02 | HomeViewModel + flow combination | Builder | DONE | T-01 |
| T-03 | TodayScreen — сборка пяти блоков | Builder | TODO | T-02, F-05 |
| T-04 | TodayScreen: CycleWidget (день цикла, фаза) | Builder | TODO | T-03 |
| T-05 | TodayScreen: WaterWidget (прогресс + быстрый лог) | Builder | TODO | T-03, W-05 |
| T-06 | TodayScreen: FastingWidget (статус/таймер) | Builder | TODO | T-03, FA-05 |
| T-07 | TodayScreen: HabitsWidget (список + отметка) | Builder | TODO | T-03 |
| T-08 | TodayScreen: StepsWidget (шаги за день + fallback) | Builder | TODO | T-03, ST-06 |
| T-09 | Empty states для каждого блока (нет данных) | Builder | TODO | T-03 |
| T-10 | UI smoke tests: Today screen flow | Test | TODO | T-03 |

---

## Этап 6: Настройки и полировка

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| S-01 | SettingsScreen — структура + навигация | Builder | DONE | F-06 |
| S-02 | Настройки воды (цель в стаканах, напоминание) | Builder | DONE | S-01, W-03, W-08 |
| S-03 | Настройки fasting (режим по умолчанию) | Builder | TODO | S-01, FA-02 |
| S-04 | Настройки привычек (список, архив) | Builder | TODO | S-01, H-02 |
| S-05 | Настройки напоминаний (вкл/выкл, время) | Builder | TODO | S-01 |
| S-06 | Onboarding (первый запуск — минимальный) | Builder | TODO | F-06 |

---


## Этап 7: Owner Verification и релизный контур MVP

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| R-01 | Заморозить MVP-scope и синхронизировать DONE/TODO в TASKS.md | Lead | TODO | Этапы 0-6 |
| R-02 | Зафиксировать чек-лист верификации и критерии pass/fail | Product + Test | TODO | R-01 |
| R-03 | Пройти базовый smoke-чек (HV-01..HV-04) на актуальном APK | Test | TODO | R-02 |
| R-04 | Пройти модульные проверки (HV-05..HV-08 по готовности модулей) | Test | TODO | R-03 |
| R-05 | Зафиксировать найденные баги и расставить приоритеты | Lead + Reviewer | TODO | R-03, R-04 |
| R-06 | Закрыть blocker/high дефекты перед baseline-сборкой APK | Builder | TODO | R-05 |
| R-07 | Запустить автоматическую сборку `:app:assembleDebug` и `qualityCheck` | Lead | TODO | R-06 |
| R-08 | Сформировать единый отчёт и указать путь к APK-артефакту | Docs | TODO | R-07 |
| R-09 | Установить APK на устройство и подтвердить финальный smoke-pass | Test | TODO | R-08 |
| R-10 | Зафиксировать baseline MVP и открыть очередь этапа 2 | Lead | TODO | R-09 |

Чек-лист верификации: `docs/MVP_VERIFICATION_CHECKLIST.md`  
Регламент сборки и отчёта: `docs/APK_BUILD_RUNBOOK.md`

---

## Этап 8: Развитие после MVP

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| P2-01 | Сбор и нормализация пользовательского фидбека после MVP | Product | TODO | R-10 |
| P2-02 | Приоритизация фидбека (impact/effort) и формирование release-backlog | Lead + Product | TODO | P2-01 |
| P2-03 | UX-polish ключевых экранов | Builder + Designer | TODO | P2-02 |
| P2-04 | Stability sprint: crash/ANR/edge cases | Builder + Test | TODO | P2-02 |
| P2-05 | Экспорт/резерв локальных данных | Builder | TODO | P2-02 |
| P2-06 | Privacy-first аналитика продукта | Architect + Builder | TODO | P2-02 |
| P2-07 | Подготовка release-подписи и versioning для сторов | Lead + Builder | TODO | P2-04 |
| P2-08 | Store-материалы (иконка, скриншоты, описания, policy) | Product + Docs | TODO | P2-07 |
| P2-09 | Закрытое тестирование в сторе и отчёт по качеству | Test + Lead | TODO | P2-08 |
| P2-10 | Публичный релиз и пост-релизный мониторинг | Lead | TODO | P2-09 |

Подробный план этапа 2: `docs/PHASE2_EXPANSION_PLAN.md`

---

## Post-MVP backlog

*(Не реализовывать без явного решения лида проекта)*

- Тёмная тема
- Экспорт данных
- Health Connect
- Виджеты главного экрана
- Аналитика и тренды
- Wear OS


