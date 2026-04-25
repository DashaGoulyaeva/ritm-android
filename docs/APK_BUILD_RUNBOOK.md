# APK_BUILD_RUNBOOK.md — Регламент автоматической сборки APK

> Цель: автоматически собирать APK и выдавать единый понятный отчёт.

---

## Текущий статус (2026-04-25)

| Проверка | Статус |
|---|---|
| `:app:assembleDebug` | ✅ PASS |
| `:app:testDebugUnitTest` | ✅ PASS |
| `qualityCheck` (lint + ktlint + detekt) | ✅ PASS (detekt-долг feature-cycle устранён) |

APK артефакт: `app/build/outputs/apk/debug/app-debug.apk`

---

## Контур задачи сборки

### Предусловия
- Ветка актуальна (`main` или рабочая release-ветка).
- Android SDK и JDK доступны в окружении.
- Нет блокирующих проблем в рабочем дереве для запуска сборки.

### Команды (Windows / PowerShell)

```powershell
.\gradlew.bat :app:assembleDebug --no-configuration-cache
.\gradlew.bat qualityCheck --no-configuration-cache
```

### Артефакты
- APK: `app/build/outputs/apk/debug/app-debug.apk`
- Опционально: SHA256 для APK

---

## Формат отчёта (обязательный)

```text
Готово, устанавливай и проверяй.

Сборка: SUCCESS
APK: app/build/outputs/apk/debug/app-debug.apk
Проверки: qualityCheck PASS/FAIL (указать факт)
Время сборки: YYYY-MM-DD HH:mm (локальное)
Следующий шаг: пройти docs/MVP_VERIFICATION_CHECKLIST.md и отправить фидбек.
```

---

## Правило ошибки

Если сборка не прошла, нельзя писать «готово». Нужно вернуть:
1. на каком шаге упало;
2. короткую причину;
3. конкретное действие для повторного запуска.
