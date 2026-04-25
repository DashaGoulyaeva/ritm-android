# DESIGN-AUDIT-003 — Аудит wireframes v3.0

## Статус: Draft | Дата: 2026-04-21

---

## 1. Краткий вердикт

`v3` качественно вырос в системности и уже ближе к production-handoff, чем `v2`:

- появился сценарный формат (flow-first) с явным разделением `screen` vs `bottom sheet`;
- выбран один основной Today (гибридный), вместо конкурирующих вариантов;
- введён общий визуальный паттерн «пульса» (`RhythmOrb`) через модули.

Но есть блокеры перед переходом к `v4`:

1. На Today отсутствует явный блок шагов (расхождение с MVP).
2. Контраст баннеров на светлых акцентах остаётся проблемным.
3. Не все обязательные решения из `DESIGN-AUDIT-002` реально доведены до конца.

---

## 2. Что стало лучше (сильные стороны)

1. **Сценарная структура страницы**
- В `v3` добавлена карта переходов и сценарные цепочки с подписью действия/результата.
- Это резко повышает понятность handoff для разработки и тестирования.
- См.: [Ритм - Wireframes v3.0.html:199](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/Ритм%20-%20Wireframes%20v3.0.html:199), [Ритм - Wireframes v3.0.html:1698](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/Ритм%20-%20Wireframes%20v3.0.html:1698).

2. **Today стал единым гибридом**
- Есть один главный вариант Today (`TodayV3Hybrid`) с компактным блоком ритма.
- Это соответствует направлению «Stack + Rhythm Rings».
- См.: [today.jsx:35](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:35).

3. **Концепция «ритма» стала системной**
- `RhythmOrb` внедрён как повторяемый паттерн across screens.
- Это реально связывает название продукта и визуальный язык.
- См.: [tokens.jsx:154](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:154), [screens.jsx:28](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:28), [screens.jsx:98](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:98), [screens.jsx:250](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:250).

4. **Лексика и локализация стали лучше**
- Меньше англицизмов в fasting (`голодание`, `воин`, `1 приём`).
- См.: [screens.jsx:609](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:609).

---

## 3. Критические проблемы

### [CRITICAL] На Today пропал явный блок шагов

В `TodayV3Hybrid` есть карточки: цикл, голодание, вода, привычки — но нет отдельной карточки шагов.

- См.: [today.jsx:51](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:51), [today.jsx:65](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:65), [today.jsx:69](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:69), [today.jsx:76](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:76).
- В MVP это обязательный пункт: шаги за день на Today.
- См.: [MVP_SCOPE.md:44](C:/Users/1/OneDrive/Документы/projects/ritm-android/MVP_SCOPE.md:44).

### [CRITICAL] Контраст на светлых акцентах остаётся ниже нормы

`Banner` поддерживает `inkOnLight`, но в экранах он не используется.

- См. API: [tokens.jsx:221](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:221), [tokens.jsx:222](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:222).
- Примеры светлых баннеров без `inkOnLight`: шаги/вода/голодание.
- См.: [screens.jsx:20](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:20), [screens.jsx:523](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:523), [screens.jsx:643](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:643).

Расчёт контрастов (белый текст):
- `#FDD835` (steps): `1.4:1`
- `#29B6F6` (water): `2.3:1`
- `#FF8A65` (fasting): `2.31:1`
- `#66BB6A` (habits): `2.36:1`

Это не проходит для обычного текста и части крупных текстов.

---

## 4. Высокий приоритет

### [HIGH] Не выполнен пункт про явную легенду цветовой семантики

В `DESIGN-AUDIT-002` требовалось закрепить «почему этот цвет = этот ритм» отдельным блоком в макете.
В `v3` такого блока нет (есть только сами цвета).

### [HIGH] Активное волнистое подчёркивание реализовано частично

- Плюс: `H` показывает волну только при `active`.
- См.: [tokens.jsx:327](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:327).
- Минус: сценарий активного контекста не включён в flow-страницу (компонент `TodayV3ActiveContext` не участвует в `mountFlow`).
- См.: [today.jsx:83](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:83), [Ритм - Wireframes v3.0.html:1698](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/Ритм%20-%20Wireframes%20v3.0.html:1698).
- Плюс: в `TodayV3Hybrid` первая карточка уже подсвечена как active по умолчанию, что ослабляет идею «показывать только текущий фокус».
- См.: [today.jsx:54](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/today.jsx:54).

### [HIGH] В flow-карте есть действие, которого нет в самом экране

Flow пишет: `в модуле голодания → «сменить окно»`, но на экране `FastingActive` такой CTA нет.

- Flow: [Ритм - Wireframes v3.0.html:1780](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/Ритм%20-%20Wireframes%20v3.0.html:1780).
- Экран: [screens.jsx:640](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:640).

### [HIGH] Для шагов всё ещё не показаны fallback-состояния сенсора/разрешений

В MVP явно требуется fallback при отсутствии сенсора/permission.

- Требование: [MVP_SCOPE.md:47](C:/Users/1/OneDrive/Документы/projects/ritm-android/MVP_SCOPE.md:47).
- В `StepsScreen` отображён только happy-path.
- См.: [screens.jsx:4](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:4).

---

## 5. Средний приоритет

### [MEDIUM] Типографика стала спокойнее, но не полностью

Улучшение есть (например section-title 600 и UI-веса), но в ряде мест остаётся много `800` и мелких капсов.

- Пример тяжёлых non-hero акцентов: [screens.jsx:267](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:267), [screens.jsx:575](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:575), [screens.jsx:623](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:623).

### [MEDIUM] Много мелкой типографики (9–10), риски читаемости

- Примеры: [screens.jsx:267](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:267), [screens.jsx:486](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/screens.jsx:486), [tokens.jsx:283](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:283).

### [MEDIUM] Введён `stepsSoft`, но в интерфейсе не применяется

- См. токен: [tokens.jsx:12](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/tokens.jsx:12).
- Это упущенная возможность смягчить жёлтые поверхности (как было запланировано в `DESIGN-AUDIT-002`).

### [MEDIUM] В макете появился большой блок post-MVP рекламы

Он явно помечен как post-MVP (это хорошо), но расположен в начале документа и визуально конкурирует с MVP-флоу.

- См.: [Ритм - Wireframes v3.0.html:177](C:/Users/1/OneDrive/Документы/projects/ritm-android/design_v3/Ритм%20-%20Wireframes%20v3.0.html:177).
- При этом монетизация в MVP не входит.
- См.: [MVP_SCOPE.md:83](C:/Users/1/OneDrive/Документы/projects/ritm-android/MVP_SCOPE.md:83).

---

## 6. Парадигмы оценки

### 6.1 Визуальная эстетика

**Хорошо**
- Единый «ритм-символ» (`RhythmOrb`) придаёт собственное лицо.
- Карта сценариев выглядит профессиональнее, чем набор независимых фреймов.

**Слабо**
- Контраст светлых баннеров.
- Частично сохраняется тяжесть визуального веса из-за комбинации жирных акцентов и плотных контуров.

### 6.2 Понятность и UX/UI

**Хорошо**
- Flow-подписи сильно повышают понятность переходов.
- Разделение `screen`/`sheet` сделано явно и читаемо.

**Слабо**
- На уровне продукта не закрыт обязательный шаговый блок в Today.
- Есть несостыковки между «что написано в flow» и «что видно на экране».

### 6.3 Соответствие платформе и техтребованиям

**Хорошо**
- Продолжен офлайн-first фокус.
- Bottom-sheet паттерны консистентны.

**Слабо**
- Нет сценариев деградации для шагов (сенсор/permission).
- Слишком мелкие размеры типографики в ряде мест для комфортного Android UX.

---

## 7. Рекомендации к v4 (приоритетный план)

1. Вернуть отдельный блок шагов на Today (обязательный MVP-пункт).
2. Включить `inkOnLight` на светлых баннерах и прогнать контрастную проверку.
3. Добавить в макет явный блок «семантика 5 цветов» (не только в токенах).
4. Довести active-wave до реальных сценариев в flow (минимум 3).
5. Синхронизировать flow-карту и UI-экраны по реальным CTA (`сменить окно` и т.п.).
6. Добавить states для шагов: `permission denied`, `sensor unavailable`, `no data yet`.
7. Применить `stepsSoft` для крупных поверхностей шага, оставить насыщенный жёлтый только для акцентов.

---

## 8. Итог

`v3` — это **сильный концептуальный апгрейд** относительно `v2` (особенно по системности и «ритм-идентичности»),  
но до готовности к handoff ещё нужен один технически дисциплинированный проход (`v4`) по accessibility, MVP-покрытию и консистентности flow ↔ UI.

---

*Аудит выполнен по файлам `design_v3/*` с сверкой на `MVP_SCOPE.md`, `PRODUCT.md` и `DESIGN-AUDIT-002.md`.*
