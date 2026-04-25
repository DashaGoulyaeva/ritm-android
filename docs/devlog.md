# devlog.md — История разработки «Ритм»

> Публичный журнал ключевых этапов разработки.

---

## 2026-04-25

- Реализован полный Today screen: 5 виджетов (Вода, Привычки, Голодание, Цикл, Шаги) с empty states.
- Реализован Steps slice: сенсор (TYPE_STEP_COUNTER), репозиторий, use cases, ViewModel, виджет, история.
- Добавлены stub-экраны настроек: Голодание, Привычки, Напоминания, Onboarding.
- Этап 4.5 (Шаги) и Этап 5 (Today integration) закрыты; Этап 6 закрыт по MVP-scope.

## 2026-04-21

- Добавлен контур релизной проверки MVP: owner verification + автоматическая сборка APK.
- Добавлен чек-лист верификации MVP: `docs/MVP_VERIFICATION_CHECKLIST.md`.
- Добавлен регламент автоматической сборки APK и отчёта: `docs/APK_BUILD_RUNBOOK.md`.
- Добавлен план второго этапа после MVP: `docs/PHASE2_EXPANSION_PLAN.md`.
- Обновлены `TASKS.md`, `ROADMAP.md` и `README.md` под новый контур.
- Приведены формулировки к единому русскоязычному стилю и нейтральным названиям.

## 2026-04-20

- Собран foundation проекта: multi-module структура, базовая навигация, Hilt, Room, DataStore, core-ui.
- Реализован первый вертикальный модуль «Вода»: быстрый лог, история, цель и reminders.
- Добавлены quality gates: lint, ktlint, detekt, unit tests.
- Настроен baseline репозитория и опубликован в GitHub.

---

Детальные продуктовые и технические решения см. в:
- `PRODUCT.md`
- `MVP_SCOPE.md`
- `ARCHITECTURE.md`
- `TASKS.md`
