# ARCHITECTURE OVERVIEW

Краткий внешний обзор архитектуры `ritm-android`.  
Полная техническая версия: [../ARCHITECTURE.md](../ARCHITECTURE.md).

## Принципы

- **Offline-first:** приложение полноценно работает без интернета.
- **Модульность:** отдельные `feature-*` для доменов продукта.
- **Чистые границы:** feature-модули не зависят друг от друга напрямую.
- **Простые пользовательские действия:** быстрый лог и минимум экранной сложности.

## Структура модулей

```text
app
core-common
core-ui
core-database
feature-home
feature-water
feature-steps
feature-habits
feature-fasting
feature-cycle
feature-settings
```

### Роли слоёв

- `app` — точка входа, NavGraph, DI wiring приложения.
- `core-common` — общие контракты и utility (например, preference keys).
- `core-ui` — тема и переиспользуемые UI-компоненты.
- `core-database` — Room database, DAO, entities, миграции.
- `feature-*` — вертикальные продуктовые модули (data/domain/presentation).

## Поток данных (на примере фичи)

```text
Room/DataStore -> Repository -> UseCase -> ViewModel -> Compose UI
```

Для фоновых напоминаний используется `WorkManager`, планирование инжектируется через DI-контракты.

## Навигация

Текущая верхнеуровневая навигация:

- `Today`
- `Привычки`
- `Цикл`
- `Настройки`

На текущем этапе fully-functional реализован water-поток (включая экран истории и настройки воды).  
Остальные домены постепенно подключаются по roadmap, включая шаги (локально, без внешних синков).

## Текущее покрытие реализации (20 апреля 2026)

- ✅ Foundation: готов
- ✅ Water: готов
- 🟡 Settings: реализован water-сегмент
- ⏳ Habits / Fasting / Cycle / Steps: в разработке
- ⏳ Полный агрегатор Today: в разработке

## Нефункциональные рамки

- minSdk: 26
- compile/targetSdk: 35
- локальное хранение данных на устройстве
- без облачной синхронизации и AI в MVP
- шаги в MVP без Google Fit / Health Connect
