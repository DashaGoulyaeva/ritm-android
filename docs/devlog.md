# devlog.md — История разработки «Ритм»

> Формат: дата | агент | что сделано | почему (если нетривиально)

---

## 2026-04-20 | Lead | Verification pass: SDK/build + quality gates

- Проверен local.properties: sdk.dir=C\\:\\Android.
- Прогон :app:assembleDebug успешный.
- Прогон qualityCheck успешный (ktlint/detekt/lint/test).
- Синхронизированы статусы в TASKS.md: F-07 → DONE, W-07 → DONE.
- L-01 (origin) и L-03 (@github/@figma) остаются BLOCKED.
## 2026-04-20 | Lead + Product + Architect + Builder | Второй проход: W-06/W-08/S-02

**L-01/L-02/L-03 preflight:**
- `L-01`: `origin` всё ещё не задан (задача остаётся `BLOCKED`).
- `L-02`: SDK переведён на `C:\\Android` в `local.properties`, конфигурация SDK проходит (`L-02` переведён в `DONE`).
- `L-03`: MCP по `@github`/`@figma` по-прежнему пустой (задача остаётся `BLOCKED`).

**P-02 Product mini-spec:**
- Добавлен `DECISIONS/MINI-SPEC-W06-W08-S02.md` для истории воды, reminder и настроек воды.

**A-02 Architect note:**
- Добавлен `DECISIONS/ADR-004-water-settings-reminder-contracts.md`.
- Зафиксирован контракт через `core-common` (`WaterReminderScheduler`), чтобы не было прямой зависимости feature→feature.

**W-06 Water history:**
- Добавлены `GetWaterHistoryEntries`, `WaterHistoryViewModel`, `WaterHistoryScreen`, `WaterHistoryUiState`.
- Добавлен переход в историю из виджета воды на Today.

**W-08 Water reminders:**
- Добавлены `WaterReminderWorker` (feature-water) и `WaterReminderSchedulerImpl` (app) + DI binding через `ReminderModule`.
- Scheduler использует unique periodic work и не создаёт дубликаты.

**S-02 Water settings:**
- Реализован экран `WaterSettingsScreen` (цель в стаканах, enable/disable reminder, время `HH:mm`).
- Добавлены `WaterSettingsRepository`, use cases и `WaterSettingsViewModel`.
- Settings route подключён в `RitmNavGraph`.

**Статусы задач обновлены в `TASKS.md`:**
- `W-06` → `DONE`
- `W-08` → `DONE`
- `S-02` → `DONE`
- `L-02` → `DONE`

**Текущее ограничение проверки:**
- `assembleDebug` падает не по SDK, а на IO-lock в `build/intermediates` (OneDrive path lock).
- `F-07` и `W-07` остаются `BLOCKED` до стабильного прогона сборки/тестов.

## 2026-04-20 | Lead + Product + Architect + Builder + Test + Docs | Первый проход по плану запуска

**L-01 Git bootstrap:**
- Инициализирован локальный git-репозиторий (`git init`).
- `origin` не задан: нет URL remote → задача зафиксирована как `BLOCKED` в `TASKS.md`.

**L-02 Android SDK wiring:**
- Создан `local.properties` c `sdk.dir=C:\\Users\\1\\AppData\\Local\\Android\\Sdk`.
- Путь SDK отсутствует на машине, `:app:assembleDebug` падает с `SDK location not found`.
- Задача зафиксирована как `BLOCKED` в `TASKS.md`.

**L-03 Plugin access check (`@github`, `@figma`):**
- Проверка через MCP вернула отсутствие подключённых ресурсов.
- Задача зафиксирована как `BLOCKED` в `TASKS.md`.

**P-01 Product mini-spec:**
- Добавлен документ `DECISIONS/MINI-SPEC-F07-quality-gates.md` (user story, acceptance, edge cases, DoD).

**A-01 Architect note:**
- Добавлен `DECISIONS/ADR-003-drift-first-pass.md` с правилами допустимого drift на первом проходе.

**B-01 Foundation quality gates (`F-07`):**
- Добавлены плагины `detekt` и `ktlint`, root-task `qualityCheck`, базовые конфиги `.editorconfig`, `detekt.yml`, `.gitignore`.
- Проверка quality gates заблокирована до установки Android SDK.

**B-02/B-03 Water completion (`W-04`, `W-05`):**
- Добавлен `WaterViewModel` со state-wiring (entries + goal + error handling).
- Добавлены `WaterTodayWidget` и `WaterQuickLogBottomSheet` (3 кнопки: вода/чай/кофе).
- Today-route переключён с чистого placeholder на экран с water quick log.

**T-01 Water tests (`W-07`):**
- Добавлены unit-тесты для `WaterRepositoryImpl` и use cases (`Delete/GetToday/GetWaterGoal/SetWaterGoal`).
- Прогон тестов заблокирован из-за отсутствующего Android SDK.

**D-01 Docs sync:**
- `TASKS.md` синхронизирован с фактом (добавлен preflight-этап и обновлены статусы Foundation/Water).
- `docs/devlog.md` обновлён этой записью.

## 2026-04-20 | Lead Agent | Инициализация проекта

**Статус репо:** только `.gitignore`, `LICENSE`, `README.md`. Android-каркаса нет.

**Что создано:**
- `DESIGN.md` — дизайн-документ (был в папке до инициализации)
- `AGENTS.md` — система агентов, роли, шаблоны задач
- `PRODUCT.md` — продуктовая концепция, JTBD, принципы UX
- `MVP_SCOPE.md` — жёсткие границы MVP
- `ARCHITECTURE.md` — стек, модули, дата-модель, навигация, naming rules
- `TASKS.md` — 45 атомарных задач разбитых по этапам (F/W/H/FA/C/T/S)
- `docs/devlog.md` — этот файл

**Решения, принятые при инициализации:**
- minSdk = 26 (Android 8.0) — баланс охвата и современного API
- Вода в стаканах, не мл — снижение когнитивной нагрузки (зафиксировано в PRODUCT.md)
- feature-* модули не зависят друг от друга — взаимодействие только через core-database или feature-home
- Fasting и Вода без отдельной вкладки в bottom nav — доступ через Today bottom sheets

**Следующий шаг:** F-01 — создание Android-проекта с multi-module структурой.

---

## 2026-04-20 | Builder Agent | Foundation F-01 → F-06

**Что создано:**

**Gradle config:**
- `gradle/libs.versions.toml` — version catalog (AGP 8.5.2, Kotlin 2.0.21, KSP, Hilt 2.51.1, Room 2.6.1, Compose BOM 2024.09.03)
- `settings.gradle.kts` — 10 subprojects included
- `build.gradle.kts` (root) — все плагины apply false
- `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.9
- `gradlew` + `gradlew.bat`
- `app/build.gradle.kts` + все 9 module `build.gradle.kts`

**F-02: Hilt:**
- `RitmApplication.kt` — `@HiltAndroidApp` + `Configuration.Provider` для HiltWorkerFactory
- `app/di/AppModule.kt` — DataStore singleton

**F-03: Room:**
- `core-database/RitmDatabase.kt` — пустая база (entities добавляются в W-01 и далее)
- `core-database/Converters.kt` — LocalDate ↔ String
- `core-database/di/DatabaseModule.kt` — Room.databaseBuilder

**F-04: DataStore:**
- `core-common/PreferenceKeys.kt` — все preference keys

**F-05: core-ui:**
- `theme/Color.kt` — нейтральная палитра + 4 модульных акцента
- `theme/Type.kt` — RitmTypography
- `theme/Theme.kt` — RitmTheme + RitmSpacing (LocalCompositionLocal)
- `components/RitmCard.kt`, `RitmButton.kt`

**F-06: Navigation:**
- `navigation/TopLevelDestination.kt` — Today/Привычки/Цикл/Настройки
- `navigation/RitmNavGraph.kt` — NavHost + Scaffold с BottomBar
- `navigation/components/RitmBottomBar.kt`
- `MainActivity.kt` — `@AndroidEntryPoint` + `RitmTheme { RitmNavGraph() }`

**Ресурсы:**
- `AndroidManifest.xml` для app и всех 9 модулей
- `res/values/strings.xml`, `themes.xml`
- `res/drawable/ic_launcher_*` (adaptive icons, minSdk=26 → только anydpi-v26)

**Важно:** `gradle/wrapper/gradle-wrapper.jar` не создан (бинарный файл).
Для первого запуска нужно: открыть проект в Android Studio — он скачает wrapper автоматически.
Или: `gradle wrapper --gradle-version 8.9` если Gradle установлен локально.

**Следующий шаг:** W-01 — WaterEntryEntity + WaterDao (первый feature slice).

---

<!-- Новые записи добавляются сверху -->

