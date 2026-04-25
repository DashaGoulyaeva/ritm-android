# ROADMAP

Публичный план разработки `Ритм` на базе [TASKS.md](./TASKS.md).  
Обновлено: **25 апреля 2026**.

## Прогресс по этапам

| Этап | Статус | Прогресс | Комментарий |
|---|---|---|---|
| Этап -1: Lead Preflight | 🟡 частично | 2/3 | `L-03` остаётся BLOCKED (доступ к plugin-интеграциям) |
| Этап 0: Foundation | ✅ DONE | 7/7 | Базовая архитектура и quality gates готовы |
| Этап 1: Вода | ✅ DONE | 8/8 | Первый вертикальный модуль закрыт |
| Этап 2: Привычки | ✅ DONE | 8/8 | CRUD, check-in, streak, история, reminder |
| Этап 3: Fasting | ✅ DONE | 9/9 | Таймер, история, уведомление |
| Этап 4: Цикл | ✅ DONE | 7/7 | Календарь, дневник дня, прогноз |
| Этап 4.5: Шаги | ✅ DONE | 8/10 | Сенсор + кэш + Today виджет; P-ST-01/A-ST-01 пропущены как TODO |
| Этап 5: Интеграция Today | ✅ DONE | 9/10 | Все виджеты + empty states; T-10 (smoke tests) — TODO |
| Этап 6: Настройки и полировка | ✅ DONE | 6/6 | Все экраны настроек и onboarding реализованы |

## Что уже сделано

- multi-module каркас приложения;
- DI через Hilt, локальное хранение через Room + DataStore;
- базовая навигация и UI foundation;
- Water slice: лог, история, цель, reminder;
- Habits slice: CRUD, check-in, streak, история, reminder;
- Fasting slice: режимы, старт/стоп, таймер, история, уведомление;
- Cycle slice: календарь, дневник дня (выделения/симптомы/настроение), прогноз;
- Steps slice: TYPE_STEP_COUNTER сенсор, кэш в Room, ViewModel, виджет;
- Today screen: 5 виджетов (вода, привычки, голодание, цикл, шаги) с empty states;
- Все настройки: вода, голодание, привычки, напоминания, onboarding;
- quality gates (`lint`, `ktlint`, `detekt`, unit tests) настроены.

## Ближайшие шаги

1. Запустить `assembleDebug` и `qualityCheck` (этап R-07).
2. Пройти owner verification checklist (HV-01..HV-08).
3. Закрыть blocker/high дефекты.
4. Сформировать baseline APK и отчёт.

## Post-MVP (не в текущем объёме)

- тёмная тема;
- экспорт данных;
- Health Connect;
- виджеты домашнего экрана;
- продвинутая аналитика и тренды;
- Wear OS.

Полный список и статусы задач: [TASKS.md](./TASKS.md).

## Owner Verification и релизный контур MVP

- Сценарии owner verification зафиксированы в `docs/MVP_VERIFICATION_CHECKLIST.md`.
- Автоматическая сборка и формат отчёта зафиксированы в `docs/APK_BUILD_RUNBOOK.md`.
- Этот контур запускается после закрытия функциональных TODO в MVP.

## Второй этап после MVP

- План развития зафиксирован в `docs/PHASE2_EXPANSION_PLAN.md`.
- Порядок: фидбек -> приоритизация -> стабильность и полировка -> подготовка к сторам -> закрытый тест -> публичный релиз.

