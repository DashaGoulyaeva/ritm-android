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
| ST-02 | StepsSensorDataSource + permission check (`ACTIVITY_RECOGNITION`) | Builder | DONE | A-ST-01 |
| ST-03 | StepsRepository (sensor + local day baseline/cache) | Builder | DONE | ST-01, ST-02 |
| ST-04 | Use cases: ObserveTodaySteps + GetStepsHistory | Builder | DONE | ST-03 |
| ST-05 | StepsViewModel + StepsUiState | Builder | DONE | ST-04 |
| ST-06 | StepsTodayWidget (интеграция на Today) | Builder | DONE | ST-05, F-05 |
| ST-07 | StepsHistoryScreen + navigation route | Builder | DONE | ST-05, F-06 |
| T-ST-01 | Unit/DAO/UI smoke tests + edge cases по шагам | Test | DONE | ST-06, ST-07 |

---

## Этап 5: Интеграция (Today screen)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| T-01 | TodayState domain model + HomeAggregator use case | Builder | DONE | W-02, H-03, FA-02, C-02, ST-04 |
| T-02 | HomeViewModel + flow combination | Builder | DONE | T-01 |
| T-03 | TodayScreen — сборка пяти блоков | Builder | DONE | T-02, F-05 |
| T-04 | TodayScreen: CycleWidget (день цикла, фаза) | Builder | DONE | T-03 |
| T-05 | TodayScreen: WaterWidget (прогресс + быстрый лог) | Builder | DONE | T-03, W-05 |
| T-06 | TodayScreen: FastingWidget (статус/таймер) | Builder | DONE | T-03, FA-05 |
| T-07 | TodayScreen: HabitsWidget (список + отметка) | Builder | DONE | T-03 |
| T-08 | TodayScreen: StepsWidget (шаги за день + fallback) | Builder | DONE | T-03, ST-06 |
| T-09 | Empty states для каждого блока (нет данных) | Builder | DONE | T-03 |
| T-10 | UI smoke tests: Today screen flow | Test | DONE | T-03 |

---

## Этап 6: Настройки и полировка

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| S-01 | SettingsScreen — структура + навигация | Builder | DONE | F-06 |
| S-02 | Настройки воды (цель в стаканах, напоминание) | Builder | DONE | S-01, W-03, W-08 |
| S-03 | Настройки fasting (режим по умолчанию) | Builder | DONE | S-01, FA-02 |
| S-04 | Настройки привычек (список, архив) | Builder | DONE | S-01, H-02 |
| S-05 | Настройки напоминаний (вкл/выкл, время) | Builder | DONE | S-01 |
| S-06 | Onboarding (первый запуск — минимальный) | Builder | DONE | F-06 |

---


## Этап 6.5: Дизайн v4/v4.1

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| D4-01 | Публичный аудит дизайна v4 и рекомендации v4.1 | Docs | DONE | Этапы 0-6 |
| D4-02 | Mini-spec применения v4: Today, Steps, Fasting, контраст и границы MVP | Product | DONE | D4-01 |
| D4-03 | ADR/technical note: mapping v4 tokens в Compose и контракты UI primitives | Architect | DONE | D4-02 |
| D4-04 | Core-ui primitives: rhythm colors, soft surfaces, RitmBanner, RhythmOrb | Builder | DONE | D4-03 |
| D4-05 | Today/Steps/Fasting v4: общий статус дня, fallback шагов, смена fasting-окна | Builder | DONE | D4-04 |
| D4-06 | Quality gate cleanup: навигация и формат core-database, выявить остаточный detekt-долг | Lead | DONE | D4-05 |
| D4-07 | Закрыть полный quality gate: `qualityCheck` + `:app:assembleDebug` после применения v4 | Lead + Test | DONE | D4-06 |

---

## Этап 7: Owner Verification и релизный контур MVP

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| R-01 | Заморозить MVP-scope и синхронизировать DONE/TODO в TASKS.md | Lead | DONE | Этапы 0-6 |
| R-02 | Зафиксировать чек-лист верификации и критерии pass/fail | Product + Test | DONE | R-01 |
| R-03 | Пройти базовый smoke-чек (HV-01..HV-04) на актуальном APK | Test | IN_PROGRESS | R-02 |
| R-04 | Пройти модульные проверки (HV-05..HV-08 по готовности модулей) | Test | IN_PROGRESS | R-03 |
| R-05 | Зафиксировать найденные баги и расставить приоритеты | Lead + Reviewer | DONE | R-03, R-04 |
| R-06 | Закрыть blocker/high дефекты (навигация, настройки вылет, стоп-голодание, конец цикла) | Builder | DONE | R-05 |
| R-07 | Запустить автоматическую сборку `:app:assembleDebug` и `qualityCheck` | Lead | DONE | R-06 |
| R-08 | Сформировать единый отчёт и указать путь к APK-артефакту | Docs | DONE | R-07 |
| R-09 | Установить APK на устройство и подтвердить финальный smoke-pass | Test | TODO | R-08 |
| R-10 | Зафиксировать baseline MVP и поставить тег v0.1.0-mvp | Lead | TODO | R-09 |

Чек-лист верификации: `docs/MVP_VERIFICATION_CHECKLIST.md`  
Регламент сборки и отчёта: `docs/APK_BUILD_RUNBOOK.md`

---

## Этап 9: V1 — Убрать stub-ощущение (V1-03)

> Цель: пользователь не должен попадать на экран "скоро" или сломанный переключатель.
> Приоритет: BLOCKER для публичного релиза.

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-03-01 | Fasting settings: wire DataStore для хранения режима по умолчанию (16:8, 18:6, 20:4, custom) | Builder | TODO | R-10 |
| V1-03-02 | Habits settings: показать список архивированных привычек (экран archive list) | Builder | TODO | R-10 |
| V1-03-03 | Reminder settings: подключить WorkManager для привычек (вечернее напоминание по времени) | Builder | TODO | R-10 |
| V1-03-04 | Reminder settings: подключить WorkManager для fasting (уведомление об окончании окна) | Builder | TODO | V1-03-03 |
| V1-03-05 | Steps history screen: реализовать список шагов по дням (Room query + LazyColumn) | Builder | TODO | R-10 |
| V1-03-06 | Today → шаги: подключить навигацию к StepsHistoryScreen из виджета | Builder | TODO | V1-03-05 |
| V1-03-07 | Today → вода: починить back navigation (Bottom nav «Сегодня» из WaterHistoryScreen) | Builder | TODO | R-10 |
| V1-03-08 | Onboarding: 3-экранный flow (Привет / Пять ритмов / Разрешения) с permission request | Builder | TODO | R-10 |
| V1-03-09 | Скрыть или заменить все TODO-заглушки из видимых пользователю экранов | Builder | TODO | V1-03-01..08 |

---

## Этап 10: V1 — Сильный первый опыт (V1-04)

> Цель: Today как главный экран для скриншотов; понятен без объяснений.

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-04-01 | Today: dark hero banner («Сегодня») с датой, днём недели и коротким статусом | Builder | TODO | Этап 9 |
| V1-04-02 | Today: заменить мелкие orb-кружки на горизонтальные карточки-пульсы по дизайну v4 | Builder | TODO | V1-04-01 |
| V1-04-03 | Today: empty state для пустого дня — "Добро пожаловать, начни с воды или привычки" | Builder | TODO | V1-04-01 |
| V1-04-04 | Вода: кнопка отмены последней записи (undo last entry) | Builder | TODO | Этап 9 |
| V1-04-05 | Вода: история с группировкой по дням + статистика 7/30 дней | Builder | TODO | Этап 9 |
| V1-04-06 | Привычки: пресеты для быстрого старта (сон, прогулка, медитация, вода) | Builder | TODO | Этап 9 |
| V1-04-07 | Привычки: расписание привычки (ежедневно / по дням недели) | Builder | TODO | Этап 9 |
| V1-04-08 | Привычки: статистика 7/30 дней в HabitDetailScreen | Builder | TODO | Этап 9 |
| V1-04-09 | Fasting: eating-window таймер (обратный отсчёт окна еды) | Builder | TODO | Этап 9 |
| V1-04-10 | Fasting: статистика сессий (завершённые, средняя длительность, текущий стрик) | Builder | TODO | Этап 9 |
| V1-04-11 | Цикл: редактирование прошлых дат начала/конца периода | Builder | TODO | Этап 9 |
| V1-04-12 | Цикл: дисклеймер "не заменяет врача" в UI | Builder | TODO | Этап 9 |
| V1-04-13 | Permission flow: объяснение ACTIVITY_RECOGNITION при первом запуске шагомера | Builder | TODO | V1-03-08 |
| V1-04-14 | Notification permission flow: запрос POST_NOTIFICATIONS с объяснением | Builder | TODO | V1-03-08 |
| V1-04-15 | Проверка на малых экранах (5") и больших (6.7"): отступы, карточки, текст | Test | TODO | V1-04-01..14 |

---

## Этап 11: V1 — Данные и доверие (V1-05)

> Цель: данные не теряются, privacy-позиция видна.

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-05-01 | Проверить все Room migration: добавить тест `MigrationTest` для schema v1→v2 и далее | Test | TODO | Этап 9 |
| V1-05-02 | Простой экспорт данных: JSON-файл всех ритмов через Share Intent | Builder | TODO | Этап 9 |
| V1-05-03 | Экран "О приложении": версия, дата сборки, privacy-позиция, дисклеймер | Builder | TODO | Этап 9 |
| V1-05-04 | Privacy policy: текст в assets + ссылка из "О приложении" и из настроек | Docs | TODO | V1-05-03 |
| V1-05-05 | Проверить сохранение данных после uninstall/reinstall (backup rules) | Test | TODO | V1-05-01 |

---

## Этап 12: V1 — Release-сборка (V1-06)

> Цель: воспроизводимая release-сборка 1.0.0.

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-06-01 | Создать keystore и настроить signing config в `app/build.gradle.kts` | Lead | TODO | Этап 11 |
| V1-06-02 | Настроить versioning: `versionCode = 1`, `versionName = "1.0.0"` | Builder | TODO | V1-06-01 |
| V1-06-03 | Настроить R8/ProGuard rules для Hilt, Room, Compose | Builder | TODO | V1-06-01 |
| V1-06-04 | Собрать release AAB: `./gradlew :app:bundleRelease` | Lead | TODO | V1-06-02, V1-06-03 |
| V1-06-05 | Проверить release-сборку на устройстве (install & smoke test) | Test | TODO | V1-06-04 |
| V1-06-06 | Обновить `docs/APK_BUILD_RUNBOOK.md` для release-сборки | Docs | TODO | V1-06-04 |

---

## Этап 13: V1 — Store-материалы (V1-07)

> Цель: страница стора выглядит как настоящий продукт.

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-07-01 | Финальная иконка высокого разрешения (512×512 PNG) для Google Play | Designer | TODO | Этап 12 |
| V1-07-02 | Feature graphic 1024×500 для Google Play | Designer | TODO | Этап 12 |
| V1-07-03 | 5 скриншотов (Today, Вода, Привычки, Цикл, Настройки) — phone portrait | Designer | TODO | Этап 10 |
| V1-07-04 | Короткое описание приложения (80 символов) | Product | TODO | Этап 12 |
| V1-07-05 | Полное описание (до 4000 знаков, русский язык) | Product | TODO | Этап 12 |
| V1-07-06 | Changelog v1.0.0 (first release notes) | Docs | TODO | Этап 12 |
| V1-07-07 | Зарегистрировать приложение в Google Play Console | Lead | TODO | V1-06-04 |
| V1-07-08 | QA store checklist: иконка, описание, скриншоты, policy, возрастной рейтинг | Test | TODO | V1-07-01..06 |

---

## Этап 14: V1 — Закрытый тест и запуск (V1-08, V1-09)

| # | Задача | Owner | Статус | Зависит от |
|---|---|---|---|---|
| V1-08-01 | Подготовить release APK для закрытых тестировщиков | Lead | TODO | Этап 12 |
| V1-08-02 | Собрать 5-20 тестировщиков и раздать сборку (Internal Testing в Play Console) | Lead | TODO | V1-08-01 |
| V1-08-03 | Собрать фидбек по 5 сценариям: первый запуск / Today / уведомления / данные / стабильность | Test | TODO | V1-08-02 |
| V1-08-04 | Классифицировать фидбек (blocker/high/medium) и закрыть критичное | Lead + Builder | TODO | V1-08-03 |
| V1-08-05 | Go/No-Go решение | Lead | TODO | V1-08-04 |
| V1-09-01 | Поставить тег `v1.0.0` и зафиксировать release notes | Lead | TODO | V1-08-05 |
| V1-09-02 | Опубликовать в Google Play (Production или Staged rollout 20%) | Lead | TODO | V1-09-01 |
| V1-09-03 | Включить пост-релизный мониторинг (Play Console crash reporting) | Lead | TODO | V1-09-02 |
| V1-09-04 | Завести backlog V1.1 по реальному фидбеку | Product | TODO | V1-09-02 |

---

## Post-MVP backlog

*(Не реализовывать без явного решения лида проекта)*

- Тёмная тема
- Health Connect (шаги + активность)
- Виджеты главного экрана
- Аналитика и тренды (графики за месяц)
- Wear OS companion
- Облачный backup (опционально, privacy-first)
- Импорт из Apple Health / Google Fit


