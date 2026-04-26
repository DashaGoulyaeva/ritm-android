# APK-AUDIT-001 — аудит `app-debug.apk`

Дата: 2026-04-26  
Артефакт: `releases/app-debug.apk`  
Пакет: `com.dashagoulyaeva.ritm`  
Версия: `0.1.0 (code 1)`  
Тип сборки: `debug` (подписано debug-сертификатом, `debuggable=true`)

---

## 1. Краткий вывод

APK подтверждает, что MVP-функционал в проекте в целом собран и концепция `Ритм` уже выражена в интерфейсе (единый Today, 5 ритмов, офлайн-first).

Но текущий `app-debug.apk` имеет несколько критичных несоответствий проектным целям и пользовательскому сценарию:

1. шаги на Android 10+ фактически не заработают из-за отсутствия `ACTIVITY_RECOGNITION` в манифесте;
2. нет runtime-флоу запроса разрешений (шаги/уведомления);
3. история fasting есть в графе, но не имеет точки входа из UI;
4. переключатели reminders для habits/fasting сохраняют флаг, но не подключены к планировщику WorkManager.

Итог: для внутренней dev-проверки APK пригоден, но как MVP-демо “для пользователя” и тем более для внешнего теста требует обязательной доработки.

---

## 2. Что хорошо (плюсы)

### 2.1 Платформа и базовый техконтур

- `minSdk=26`, `targetSdk=35`, `compileSdk=35` соблюдены.
- `allowBackup=false` выставлен.
- Интернет не запрашивается, что соответствует offline-first.
- WorkManager/Hilt/Room/DataStore корректно вшиты в APK.

### 2.2 Соответствие продуктовой идее

- Today реализован как единый экран с 5 ритмами: цикл, голодание, вода, шаги, привычки.
- Визуально поддержан мотив “ритма” через hero и круговые орбиты.
- Цветовая семантика ритмов заведена централизованно (`Cycle/Water/Fasting/Steps/Habits + soft`).

### 2.3 В целом по UX

- Быстрые действия на Today доступны без лишних переходов (вода, голодание, переходы в историю для воды/шагов).
- Для шагов есть fallback-парадигма (`PermissionDenied/SensorUnavailable/NoDataYet`) на уровне state-модели.
- Навигация простая и предсказуемая (Today/Habits/Cycle/Settings + detail routes).

---

## 3. Критичные проблемы и несоответствия

## [CRITICAL] C-01: нет `ACTIVITY_RECOGNITION` в manifest при обязательной проверке в steps

### Факт

- В манифесте приложения есть `POST_NOTIFICATIONS`, `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, но нет `ACTIVITY_RECOGNITION`.
- Шагомер в коде требует `Manifest.permission.ACTIVITY_RECOGNITION`.

### Доказательства

- `app/src/main/AndroidManifest.xml` (permissions) — отсутствует `ACTIVITY_RECOGNITION`.
- `feature-home/.../StepsSensorDataSource.kt:24-29` — явная проверка `ACTIVITY_RECOGNITION`.

### Риск

На Android 10+ шаги будут недоступны по умолчанию, что ломает один из обязательных MVP-контрактов.

---

## [CRITICAL] C-02: отсутствует runtime-запрос разрешений (шаги и уведомления)

### Факт

- По коду нет `rememberLauncherForActivityResult`/`ActivityResultContracts.RequestPermission`.
- `WaterReminderWorker` просто молча прекращает работу при отсутствии `POST_NOTIFICATIONS`.
- Для шагов permission flow также отсутствует.

### Доказательства

- Глобальный поиск по `app`/`feature-*`: нет runtime permission launcher.
- `feature-water/.../WaterReminderWorker.kt:35-37` — ранний `return`, если нет permission.

### Риск

Пользователь не получает понятный сценарий “как включить фичу”, фичи выглядят “сломанными”.

---

## [HIGH] C-03: `PermissionDenied` у шагов практически недостижим, ошибки смешиваются

### Факт

- `StepsSensorDataSource` и для “нет permission”, и для “нет сенсора” отправляет `-1`.
- `StepsViewModel` интерпретирует `-1` как `isAvailable=false`, но `error` не заполняет.
- `StepsUiState` определяет `PermissionDenied` только через `error.contains("permission")`.

### Доказательства

- `StepsSensorDataSource.kt:37-49`
- `StepsViewModel.kt:28-31`
- `StepsUiState.kt:17-19`

### Риск

Пользователь получает не тот fallback-текст (permission может отображаться как sensor unavailable), ухудшается UX и диагностируемость.

---

## [HIGH] C-04: маршрут `fasting_history` есть, но нет пользовательского перехода

### Факт

- Route `fasting_history` объявлен и composable подключен.
- Из экранов нет `navigate("fasting_history")` или эквивалентного callback.

### Доказательства

- `app/.../RitmNavGraph.kt:60, 143-145`
- Глобальный поиск переходов на `fasting_history` — отсутствуют.

### Риск

MVP-требование “история завершённых сессий fasting” формально реализовано в коде, но недоступно пользователю.

---

## [HIGH] C-05: reminders habits/fasting не подключены к планировщику

### Факт

- `ReminderSettingsViewModel` меняет только DataStore-флаги.
- `HabitReminderWorker` и `FastingCompletionWorker` существуют, но вызовы enqueue/cancel отсутствуют.
- В DI привязан только `WaterReminderScheduler`.

### Доказательства

- `feature-settings/.../ReminderSettingsViewModel.kt:45-54`
- `feature-habits/.../HabitReminderWorker.kt` (только worker-класс)
- `feature-fasting/.../FastingCompletionWorker.kt` (только worker-класс)
- `app/.../ReminderModule.kt` — bind только water scheduler

### Риск

UI обещает управление напоминаниями, но функционально они не работают.

---

## [HIGH] C-06: “режим fasting по умолчанию” сохраняется, но не применяется в рабочем flow

### Факт

- `FastingSettingsViewModel` сохраняет `FASTING_DEFAULT_PLAN` в DataStore.
- `FastingViewModel` стартует с локального default `PLAN_16_8` и DataStore не читает.

### Доказательства

- `feature-settings/.../FastingSettingsViewModel.kt:38-56`
- `feature-fasting/.../FastingUiState.kt` (default `PLAN_16_8`)
- `feature-fasting/.../FastingViewModel.kt:73-80`

### Риск

Настройка есть в UI, но пользователь не получает ожидаемый эффект в основном сценарии.

---

## 4. Существенные замечания (не блокеры, но важны)

## [MEDIUM] M-01: APK тяжёлый для debug (18.7 MB) и перегружен dex

### Факт

- Размер APK: `18,692,678` bytes.
- `TOTAL_REFS=145,229`, `MAX_DEX_REFS=65,508` (почти лимит на одном dex).
- 25 dex-файлов.

### Ключевая причина

- В `core-ui` подключен `api(libs.androidx.compose.material.icons)` (extended icons), что сильно раздувает dex.

### Доказательства

- `core-ui/build.gradle.kts:34`
- `apkanalyzer dex packages` показывает гигантский вклад `androidx.compose.material.icons`.

### Комментарий

Для debug это ожидаемо, но для релизной стабильности и cold-start стоит перейти на выборочные иконки.

---

## [MEDIUM] M-02: в debug APK присутствуют dev-компоненты

### Факт

- `android:debuggable="true"`
- `androidx.compose.ui.tooling.PreviewActivity` в manifest
- debug-сертификат `CN=Android Debug`

### Доказательства

- `app/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml:27, 156-157`
- `apksigner verify --print-certs` (debug cert)

### Комментарий

Нормально для `app-debug.apk`, но нельзя использовать как артефакт внешнего теста/релиза.

---

## [MEDIUM] M-03: документация и фактическая архитектура частично расходятся

### Факт

- В `ARCHITECTURE.md` заявлен модуль `feature-steps`.
- В проекте отдельного модуля нет, steps живут в `feature-home`.

### Доказательства

- `ARCHITECTURE.md:33`
- В корне проекта отсутствует `feature-steps/`.

### Риск

Снижается точность handoff и понимание границ модулей.

---

## [MEDIUM] M-04: `KNOWN_ISSUES.md` частично устарел

### Факт

- Файл утверждает, что `FastingSettings` и `Habits archive` — stubs.
- По коду есть реальное сохранение fasting plan и рабочий экран архива привычек.
- Файл утверждает, что `StepsHistory` — stub; по коду это полноценный экран списка.

### Доказательства

- `docs/KNOWN_ISSUES.md:9-14`
- `feature-settings/.../FastingSettingsViewModel.kt`
- `feature-habits/.../HabitsArchiveScreen.kt`
- `feature-home/.../StepsHistoryScreen.kt`

### Риск

Команда может принимать решения на основании неактуальной картины.

---

## [LOW] M-05: есть легаси-экран Today в `app/navigation`, не используемый в текущем flow

### Факт

- `app/navigation/TodayScreen.kt` содержит старый водный-only сценарий.
- Основной flow использует `feature-home.presentation.todayScreen`.

### Доказательства

- `app/navigation/TodayScreen.kt`
- `app/navigation/RitmNavGraph.kt:21, 64-67`

### Риск

Шум в кодовой базе, риск случайного регресса при рефакторинге.

---

## 5. Дизайн-аудит (по реализованному APK)

## 5.1 Визуальная эстетика

### Сильные стороны

- У дизайна есть узнаваемый мотив (`ритм/пульс`) через hero + орбиты.
- Цветовая кодировка ритмов стабильна и читается.
- Карточки и soft-цвета дают цельный “дневниковый” характер.

### Слабые стороны

- Типографика местами перегружена `SemiBold/Bold` для рабочих состояний, из-за чего тяжелеет интерфейс.
- Минимальный служебный размер текста `labelSmall=10sp` — спорно для реального чтения.
- Набор “тонкая линия + жирный шрифт” местами дисбалансный (визуально “громкий” текст при деликатных рамках).

### Доказательства

- `core-ui/theme/Type.kt:23,30,37,45,52,92-95`
- `feature-home/presentation/TodayScreen.kt:293-304`
- `core-ui/components/RitmCard.kt:62,123-124`

---

## 5.2 Понятность UX/UI

### Сильные стороны

- Today структурирован по смыслу: пользователь видит ритмы дня и основные метрики в одном месте.
- Вода и шаги имеют прямой переход в историю, есть fast action.

### Проблемы

- Для критичных permissions нет явного guided-flow.
- History fasting недоступна из пользовательского пути.
- Onboarding route есть, но в текущем запуске не вызывается автоматически.

### Доказательства

- Нет вызовов runtime request API по проекту.
- `RitmNavGraph.kt:128-136` (route есть), но `navigate("onboarding")` не найден.
- `RitmNavGraph.kt` (есть route fasting history, но нет перехода на него из UI действий).

---

## 5.3 Соответствие современным реалиям платформы

### Что ок

- API 35/26, Compose, Hilt, WorkManager, Room/DataStore — современный стек.

### Что не ок

- Permission model Android 10+/13+ закрыт неполно (steps/notifications).
- Debug-артефакт содержит dev-активности и debug signing (ожидаемо для debug, но не для внешнего APK).

---

## 6. Смысловые/продуктовые несоответствия

- Принцип “мягкий, нейтральный тон” соблюдён не везде: встречается формально-вежливая и более директивная формулировка (`“Не забудьте…”`, `“Как вы себя чувствуете?”`) вместо единого “ты”-тона.
- Настройки, которые пользователь воспринимает как рабочие (`fasting default`, reminders), частично не подключены к основным сценариям.

Доказательства:

- `feature-habits/.../HabitReminderWorker.kt:33`
- `feature-cycle/.../CycleDayJournalScreen.kt:206`
- `feature-settings/.../FastingSettingsViewModel.kt` vs `feature-fasting/.../FastingViewModel.kt`

---

## 7. Приоритетный план исправлений

1. Добавить `ACTIVITY_RECOGNITION` в manifest + полноценный runtime permission flow для шагов.
2. Добавить runtime permission flow для `POST_NOTIFICATIONS` с понятным UX (не silent-fail).
3. Подключить route в `fasting_history` из Today/fasting sheet.
4. Реально подключить reminders habits/fasting к WorkManager (schedule/cancel + persist).
5. Сделать раздельные fallback-состояния шагов (permission vs sensor) на уровне datasource/viewmodel.
6. Привязать `FASTING_DEFAULT_PLAN` к старту fasting-сессии.
7. Актуализировать `KNOWN_ISSUES.md` и `ARCHITECTURE.md`.
8. Для release-профиля оптимизировать размер: ограничить `material-icons-extended`, проверить shrink/obfuscation и R8-результат.

---

## 8. Статус по результатам аудита

**Статус: условно-проходной для внутренней dev-проверки, неготовый для внешнего MVP-теста без доработок из раздела 7 (пункты 1-6 обязательны).**

