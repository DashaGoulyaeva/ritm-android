# Ритм (ritm-android)

`Ритм` — офлайн-first Android-приложение для ежедневного отслеживания ритмов и самочувствия в одном месте: **привычки, цикл, вода, fasting и шаги**.  
Ключевая идея продукта: вместо разрозненных трекеров у пользователя есть один экран **Today** с целостным статусом дня.

## Зачем этот проект

- объединить 5 ритмов в один понятный дневной контур;
- снизить когнитивную нагрузку (быстрые действия в 1-2 тапа);
- хранить данные локально на устройстве (без облака в MVP);
- давать мягкий, нейтральный UX без давления и без медицинских обещаний.

## Текущий статус (на 21 апреля 2026)

MVP в активной разработке. Базовый каркас и первый вертикальный модуль уже собраны.

| Направление | Статус | Комментарий |
|---|---|---|
| Foundation (этап 0) | ✅ DONE | multi-module, Compose, Hilt, Room, DataStore, базовая навигация, quality gates |
| Вода (этап 1) | ✅ DONE | быстрый лог (вода/чай/кофе), цель, история, reminder |
| Настройки воды (S-02) | ✅ DONE | экран настроек цели и напоминаний |
| Шаги (новый этап) | ⏳ TODO | запланирован MVP-слайс без Google Fit/Health Connect |
| Привычки | ⏳ TODO | следующий крупный блок |
| Fasting | ⏳ TODO | в очереди после привычек |
| Цикл | ⏳ TODO | в очереди после fasting |
| Интегрированный Today | ⏳ TODO | полноценный агрегатор всех модулей ещё не реализован |

## MVP-границы

**Входит в MVP:** локальное отслеживание (привычки, цикл, вода, fasting, шаги), единый Today, локальные уведомления, локальное хранение.  
**Не входит в MVP:** аккаунты и облако, AI-рекомендации, Health Connect, монетизация, соцфункции, медицинские режимы/обещания.

Шаги в MVP: локальный сбор с системного сенсора устройства, без синхронизаций со сторонними сервисами.

Подробно: [MVP_SCOPE.md](./MVP_SCOPE.md)

## Технологический стек

- Kotlin + Jetpack Compose
- Navigation Compose
- Hilt
- Room + DataStore
- Coroutines / Flow
- WorkManager
- JUnit / Compose UI Tests / Detekt / Ktlint

## Архитектура (кратко)

Проект модульный: `app`, `core-*`, `feature-*`.  
Правило: feature-модули не зависят друг от друга напрямую, интеграция идёт через корневую навигацию и общие контракты/данные.

- Внешний обзор: [docs/ARCHITECTURE_OVERVIEW.md](./docs/ARCHITECTURE_OVERVIEW.md)
- Полная внутренняя спецификация: [ARCHITECTURE.md](./ARCHITECTURE.md)

## Локальный запуск

### Требования

- Android Studio (актуальная стабильная)
- JDK 17
- Android SDK 35 (compile/target), minSdk 26

### Команды

```bash
# Сборка debug
./gradlew :app:assembleDebug

# Quality gates (lint + ktlint + detekt + unit tests)
./gradlew qualityCheck
```

Для Windows PowerShell используйте `.\gradlew.bat` вместо `./gradlew`.

## Документация проекта

- [PRODUCT.md](./PRODUCT.md) — продуктовый смысл и JTBD
- [MVP_SCOPE.md](./MVP_SCOPE.md) — жёсткие границы MVP
- [ARCHITECTURE.md](./ARCHITECTURE.md) — техническая архитектура
- [TASKS.md](./TASKS.md) — атомарный план реализации
- [ROADMAP.md](./ROADMAP.md) — публичный прогресс и этапы
- [CONTRIBUTING.md](./CONTRIBUTING.md) — как контрибьютить
- [docs/devlog.md](./docs/devlog.md) — хронология изменений
- [docs/HUMAN_VERIFICATION.md](./docs/HUMAN_VERIFICATION.md) - manual QA checklist
- [docs/APK_DELIVERY_RUNBOOK.md](./docs/APK_DELIVERY_RUNBOOK.md) - AI APK build/report protocol
- [docs/PHASE2_EXPANSION_PLAN.md](./docs/PHASE2_EXPANSION_PLAN.md) - post-MVP expansion plan

## MVP validation and APK delivery

1. Manual checklist: [docs/HUMAN_VERIFICATION.md](./docs/HUMAN_VERIFICATION.md)
2. AI APK build/report runbook: [docs/APK_DELIVERY_RUNBOOK.md](./docs/APK_DELIVERY_RUNBOOK.md)
3. Post-MVP stage plan: [docs/PHASE2_EXPANSION_PLAN.md](./docs/PHASE2_EXPANSION_PLAN.md)

## Важное ограничение

`Ритм` не является медицинским сервисом. Приложение помогает вести дневник и видеть ритмы, но не даёт медицинских диагнозов и гарантий.

## Лицензия

Проект распространяется по [MIT License](./LICENSE).
