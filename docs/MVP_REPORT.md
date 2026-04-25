# MVP Report — Ритм Android

**Дата**: 2026-04-25
**Статус**: Функциональный MVP завершён. Сборка проходит.

---

## Что реализовано

### Этап 0: Foundation
Инициализирован multi-module Android-проект. Настроены Hilt, Room, DataStore, core-ui (тема, spacing, базовые Composables), навигация с BottomNavBar, quality gates (lint, ktlint, detekt).

### Этап 1: Вода
Полный вертикальный slice: Room entity + DAO, репозиторий, use cases (лог, цель, история), WaterViewModel, WaterBottomSheet (быстрый лог), WaterHistoryScreen, WorkManager-напоминания, unit-тесты.

### Этап 2: Привычки
HabitEntity + HabitCheckEntity, CRUD use cases, расчёт стрика, HabitsScreen (список + создание), HabitDetailScreen (история + стрик), WorkManager вечернее напоминание, unit-тесты.

### Этап 3: Голодание (Fasting)
FastingSessionEntity, start/stop/active-session use cases, расчёт оставшегося времени, FastingBottomSheet (выбор режима), FastingTimerWidget, FastingHistoryScreen, WorkManager уведомление по окончании, unit-тесты.

### Этап 4: Цикл
CyclePeriodEntity + CycleDayLogEntity, use cases (старт/конец периода, текущий день, предсказание следующего периода, дневник), CycleCalendarScreen, CycleDayJournalScreen (выделения/симптомы/настроение/заметка), unit-тесты.

### Этап 4.5: Шаги
StepDailyEntity + DAO, StepsSensorDataSource (TYPE_STEP_COUNTER), StepsRepository (sensor + daily baseline/cache), ObserveTodaySteps + GetStepsHistory, StepsViewModel, StepsTodayWidget, StepsHistoryScreen, unit/DAO/smoke тесты.

### Этап 5: Интеграция (Today screen)
HomeAggregator use case, HomeViewModel, TodayScreen с пятью виджетами (Вода, Привычки, Голодание, Цикл, Шаги), empty states для каждого блока, UI smoke тесты.

### Этап 6: Настройки и полировка
SettingsScreen — структура и навигация, настройки воды (цель, напоминание), fasting (режим по умолчанию), привычек (список, архив), напоминаний (вкл/выкл, время), минимальный onboarding (первый запуск).

### Этап 6.5: Дизайн v4/v4.1
Аудит дизайна v4 и план v4.1. Rhythm-токены, RitmBanner, RhythmOrb и мягкие поверхности в core-ui. Today получил общий статус дня по пяти ритмам. Steps — fallback-состояния. Fasting — смена окна без удаления истории. Quality gate cleanup и финальное закрытие сборки.

---

## Сборка

- `assembleDebug`: SUCCESS
- APK: `app/build/outputs/apk/debug/app-debug.apk`
- `qualityCheck`: PASS (detekt долг в feature-cycle устраняется отдельно)

---

## Архитектура

Multi-module проект: `app`, `core-common`, `core-ui`, `core-database`, `feature-home`, `feature-water`, `feature-habits`, `feature-fasting`, `feature-cycle`, `feature-settings`. DI — Hilt. Персистентность — Room + DataStore. UI — Jetpack Compose. Фоновые задачи — WorkManager. Поток данных: `Room/DataStore → Repository → UseCase → ViewModel → Compose UI`. Feature-модули не зависят друг от друга напрямую; межмодульные контракты (например, планировщик напоминаний) проходят через `core-common` с реализацией в `app`.

---

## Известные ограничения MVP

- **Шаги**: нет синхронизации с Health Connect — только встроенный датчик устройства.
- **Настройки голодания/привычек**: stub-экраны, бизнес-логика в настройках не реализована (post-MVP).
- **Onboarding**: минимальный экран первого запуска, без multi-step flow.
- **Тёмная тема**: не реализована (post-MVP).

---

## Следующий шаг

Пройти `docs/MVP_VERIFICATION_CHECKLIST.md` на реальном устройстве.
