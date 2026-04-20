# CLAUDE.md — Инструкции для агентов в проекте Ритм

## Обязательно прочитать перед началом работы

1. `AGENTS.md` — роли, границы, шаблон задачи
2. `ARCHITECTURE.md` — стек, модули, naming rules
3. `MVP_SCOPE.md` — что входит, что нет
4. `TASKS.md` — текущие задачи и статусы

## Рабочий процесс

- Не расширяй scope задачи. Видишь что-то рядом — добавь в TASKS.md, не трогай.
- После завершения задачи: обнови статус в TASKS.md + запись в docs/devlog.md.
- Не нарушай зависимости между задачами (колонка "Зависит от" в TASKS.md).
- Язык кода: Kotlin. Язык UI-текстов: русский.

## Структура проекта

Android multi-module проект. Package: `com.dashagoulyaeva.ritm`

Модули: `app`, `core-common`, `core-ui`, `core-database`, `feature-home`, `feature-cycle`, `feature-water`, `feature-fasting`, `feature-habits`, `feature-settings`

## Стек

Kotlin + Jetpack Compose + Navigation Compose + Hilt + Room + DataStore + WorkManager

## Ключевые правила

- `minSdk = 26`, `targetSdk = 35`
- feature-* модули НЕ зависят друг от друга напрямую
- Room entities в `core-database`, domain models в `feature-*/domain/model/`
- Даты хранятся как `String "yyyy-MM-dd"` в Room, `Long` для timestamps
