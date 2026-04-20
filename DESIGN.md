# 📱 Ритм — Дизайн-документ Android MVP

> Собрано из переписки. Всё, что нужно для старта разработки через вайбкодинг.

---

## 0. GitHub-референсы (изучить перед кодингом)

| Что | Репозиторий | Зачем смотреть |
|---|---|---|
| Привычки | [iSoron/uhabits](https://github.com/iSoron/uhabits) | Зрелый каркас, напоминания, офлайн |
| Вода | [brandyodhiambo/Quench](https://github.com/brandyodhiambo/Quench) | Современный Android-стек, WorkManager |
| Fasting | [Husseinfo/interfast](https://github.com/Husseinfo/interfast) | Логика time-restricted feeding |
| Цикл | [Tnxec2/EasyCycle2](https://github.com/Tnxec2/EasyCycle2) | Базовый цикл-трекер на Compose |
| Цикл+ | [MangoKrish/petal-android](https://github.com/MangoKrish/petal-android) | Идеи по фазам и симптомам |

---

## 1. Что это за продукт

**Название:** Ритм
**Package name:** `com.dashagoulyaeva.ritm`
**Репо:** `ritm-android` (новый)
**Платформа:** Android
**Язык интерфейса:** Русский

**Суть:** одно приватное офлайн-first приложение, которое объединяет четыре ежедневных ритма тела и поведения — вместо четырёх разрозненных приложений.

**Главная идея:** у пользователя не четыре проблемы, а один день и одно тело. Поэтому главный экран — это не набор вкладок, а единый статус дня.

**Для кого:** для любого человека, которому важно понимать закономерности своего тела и поведения. Гендер-нейтральное.

---

## 2. Зафиксированные продуктовые решения

### Дизайн
- Не «девочковый», не корпоративный
- Нейтральный, мягкий, с собственным лицом
- Референс — Clatch по принципу дневника, но не по визуалу
- Светлая тема на старте

### Привычки
- Универсальные: и «делать», и «не делать»
- Примеры: лечь до 01:00, не курить, не заедать стресс, медитировать

### Цикл
- Нужен не только календарь, но и **дневник состояния по дням** (как в Clatch)
- Поля дневника: выделения, физические симптомы, настроение, заметка
- Без медицинских обещаний: «прогноз», а не «точная дата»

### Вода
- **Никаких миллилитров** в основном интерфейсе
- Быстрый лог: стакан воды / стакан чая / стакан кофе
- Цель — снимать когнитивную нагрузку, а не добавлять её

### Fasting
- Короткие окна: 12/12, 14/10, 16/8
- Длинные окна: 18/6, 20/4, 23/1 (OMAD)
- Ручной старт / стоп
- История завершённых окон

### Уведомления
- Мягкие, с лёгким юмором
- Без давления и дисциплинарного тона
- Примеры: «Кажется, ты сегодня ещё не пил(а) ничего. Вода тебя ждёт 🌊»

---

## 3. MVP: что входит, что нет

### ✅ Входит
- **Today screen** — главный экран, единый статус дня
- **Трекер привычек** — создание, отметка, история, стрик
- **Цикл** — календарь, дневник состояния, прогноз
- **Вода** — быстрый лог стаканами, дневная цель, история
- **Fasting** — выбор режима, таймер, история
- **Локальные уведомления** — вода, fasting, привычки, дайджест
- **Локальное хранение** — всё на устройстве, без облака

### ❌ Не входит (post-MVP)
- Аккаунты и регистрация
- Облачная синхронизация
- Health Connect
- AI-рекомендации
- Монетизация
- Социальные функции
- Беременность / планирование зачатия
- Сложная аналитика и экспорт PDF
- Медицинские обещания и «точные предсказания»

---

## 4. Экранная структура

### Экран 1: Today (главный)
Единый ежедневный центр. Всё важное — без скролла.

**Блоки:**
- Цикл: день цикла, прогноз
- Fasting: статус окна, таймер
- Вода: прогресс к цели
- Привычки: список на сегодня, галочки

**Главное действие:** быстрый лог через bottom sheet (тапнул → отметил → закрыл)

---

### Экран 2: Цикл
- Календарь с отметками дней
- Вход в дневник дня (симптомы, настроение, выделения, заметка)
- История + прогноз следующего цикла

### Экран 3: Привычки
- Список активных привычек
- Создание привычки
- История и стрик по каждой

### Экран 4: История / Дашборд
- Общий календарь: цикл + fasting + вода по дням
- Тренды

### Экран 5: Настройки
- Управление привычками
- Напоминания
- Режим fasting по умолчанию
- Цель по воде
- Приватность

---

## 5. Стек технологий

| Слой | Технология |
|---|---|
| Язык | Kotlin |
| UI | Jetpack Compose |
| Навигация | Navigation Compose |
| DI | Hilt |
| БД | Room |
| Настройки | DataStore |
| Async | Coroutines / Flow |
| Фоновые задачи | WorkManager |
| Тесты | JUnit, Turbine, Compose UI Tests |

---

## 6. Структура модулей

```
ritm-android/
├── app/
├── core-common/
├── core-ui/           ← тема, цвета, компоненты
├── core-database/     ← Room, DAO, entities
├── feature-home/      ← Today screen + агрегатор
├── feature-cycle/     ← цикл + дневник
├── feature-water/     ← вода
├── feature-fasting/   ← fasting
├── feature-habits/    ← привычки
└── feature-settings/
```

Внутри каждой feature:
```
feature-X/
├── data/      ← repository, datasource
├── domain/    ← use cases, models
└── presentation/ ← screen, viewmodel, uistate
```

---

## 7. Ключевые сущности данных

```kotlin
// Привычки
data class Habit(id, title, type: POSITIVE/AVOID, frequency, reminderConfig?, isArchived)
data class HabitCheck(id, habitId, date, status: DONE/SKIPPED/FAILED)

// Цикл
data class CyclePeriod(id, startDate, endDate, flowIntensityByDay)
data class CycleDayLog(id, date, flowIntensity, symptoms: List<String>, mood, notes?)

// Вода
data class WaterEntry(id, timestamp, type: WATER/TEA/COFFEE)
data class WaterGoal(dailyTarget: Int) // в стаканах

// Fasting
data class FastingSession(id, planType, startDateTime, plannedEndDateTime, actualEndDateTime?, status)

// Дневной агрегат
data class TodayState(cycleDay, fastingStatus, waterProgress, habits: List<HabitItem>)
```

**Ключевой момент:** `TodayState` — это сердце продукта. `HomeAggregator` собирает данные из всех четырёх доменов в один объект для главного экрана.

---

## 8. Агентная схема для вайбкодинга

### Роли агентов

| Агент | Что делает | Что не делает |
|---|---|---|
| **Lead** | Дробит задачи, контролирует порядок интеграции | Не пишет большие куски кода самовольно |
| **Product** | Пишет user story, acceptance criteria, edge cases | Не трогает production code |
| **Architect** | Фиксирует data model, модули, use cases | Не лезет в финальную вёрстку |
| **Builder** | Пишет код строго в рамках задачи | Не меняет соседние фичи «заодно» |
| **Test** | Unit-тесты, UI smoke tests | Не меняет поведение продукта |
| **Reviewer** | Чистит diff, смотрит архитектурный drift | Не переписывает половину ради эстетики |
| **Docs** | README, ADR, devlog, changelog | — |

### Правило постановки задачи агенту

**Плохо:** «Сделай fasting»

**Хорошо:**
> «Добавь вертикальный slice для fasting. Нужны: FastingSession entity, DAO, Repository, use cases StartFasting/StopFasting/GetActiveSession, экран с выбором режима (12/12, 14/10, 16/8, 18/6, 20/4, 23/1), таймер активного окна, история завершённых сессий. Не трогай навигацию других фич. Добавь unit tests для расчёта оставшегося времени. После выполнения обнови docs/devlog.md.»

### Пайплайн работы

```
Lead получает задачу
  → Product пишет mini-spec
    → Architect пишет technical note
      → Builder реализует slice
        → Test проверяет
          → Reviewer чистит и принимает
```

---

## 9. Порядок разработки (итерации)

| Этап | Содержание |
|---|---|
| **0. Foundation** | Каркас проекта, тема, навигация, Room, DataStore, Hilt, базовые UI primitives, lint/test skeleton |
| **1. Вода** | Entity → DAO → Repository → Use Cases → Screen → Quick add (стаканы) → Daily goal → Reminder |
| **2. Привычки** | Habit entity → Create/Edit → Today list → Check-in → История → Стрик |
| **3. Fasting** | Plan selection → Session entity → Таймер → Start/Stop → История → Notifications |
| **4. Цикл** | Period log → Дневник дня → Calendar → Расчёт day-of-cycle → Прогноз |
| **5. Интеграция** | Today screen aggregator → Daily summary → Empty states |
| **6. Полировка** | Onboarding, ошибки, accessibility, настройки напоминаний, QA |

**Почему такой порядок:** начинаем с воды — она самая простая, быстро стабилизирует архитектуру и даёт ощущение работающего продукта.

---

## 10. Документы в корне репозитория

```
ritm-android/
├── AGENTS.md       ← кто за что отвечает, правила для агентов
├── PRODUCT.md      ← продуктовая концепция, JTBD, гипотезы
├── MVP_SCOPE.md    ← жёсткий список: входит / не входит / потом
├── ARCHITECTURE.md ← модули, слои, правила naming
├── TASKS.md        ← атомарные задачи с owner и статусом
├── DECISIONS/      ← ADR: почему Room, почему офлайн, почему без мл
└── docs/devlog.md  ← что сделано, что изменилось, почему
```

---

## 11. Стартовый промпт для Codex (готов к копипасту)

Этот промпт запускает lead agent и настраивает его на правильный режим работы:

```
Работаем над новым Android-приложением.
Название: Ритм
Package: com.dashagoulyaeva.ritm
Репо: ritm-android (новый)

Твоя роль: lead agent. Не начинай писать всё подряд.
Сначала зафиксируй структуру, документы, задачи. Потом реализацию.

Продукт: офлайн-first Android-приложение — единый трекер ежедневных ритмов.
4 модуля: привычки (универсальные), цикл (дневник + календарь), вода (стаканами, без мл), fasting (включая 23:1).
Главный принцип: не 4 разрозненных трекера, а один экран Today с общим статусом дня.

Дизайн: нейтральный, гендер-нейтральный, мягкий. Не девочковый. Уведомления — мягкие с юмором.

Стек: Kotlin, Jetpack Compose, Navigation Compose, Room, DataStore, Hilt, Coroutines/Flow, WorkManager.

Модули: app, core-common, core-ui, core-database, feature-home, feature-cycle, feature-water, feature-fasting, feature-habits, feature-settings.

В MVP входит: Today, цикл, вода, fasting, привычки, локальные уведомления, локальное хранение.
В MVP НЕ входит: аккаунты, облако, Health Connect, AI, монетизация, соцфункции, медицинские обещания.

Что сделать на первом проходе:
1. Проверь, есть ли каркас Android-проекта.
2. Создай документы: AGENTS.md, PRODUCT.md, MVP_SCOPE.md, ARCHITECTURE.md, TASKS.md, docs/devlog.md.
3. Подготовь Foundation: тема, навигация, Room setup, Hilt setup, базовые UI primitives.
4. Нарежь MVP на атомарные вертикальные slices. Каждый slice: цель, границы, файлы, критерии готовности.

Если чего-то не хватает (путь к папке, имя репо) — задай только блокирующие вопросы, не выдумывай.

Формат первого ответа:
1. Что нашёл
2. Что создал
3. Предложенная структура
4. Блокирующие вопросы
5. Первые 10 атомарных задач
```

---

*Документ создан: 20 апреля 2026*
*На основе: переписки по проектированию приложения «Ритм»*
*GitHub-референсы: [uhabits](https://github.com/iSoron/uhabits) · [Quench](https://github.com/brandyodhiambo/Quench) · [interfast](https://github.com/Husseinfo/interfast) · [EasyCycle2](https://github.com/Tnxec2/EasyCycle2) · [petal-android](https://github.com/MangoKrish/petal-android)*
