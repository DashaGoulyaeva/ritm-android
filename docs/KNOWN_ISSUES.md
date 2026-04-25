# KNOWN_ISSUES.md — Известные ограничения и технический долг

> Статус на 2026-04-25. Обновлять по мере закрытия.

## Критичность: Medium (не блокируют MVP)

| # | Описание | Файл/модуль | Планируется |
|---|---|---|---|
| KI-01 | Настройки голодания — stub, нет реальной логики сохранения режима по умолчанию | feature-settings/FastingSettingsScreen | Post-MVP S-03 |
| KI-02 | Настройки привычек — stub, нет списка архива | feature-settings/HabitsSettingsScreen | Post-MVP S-04 |
| KI-03 | Напоминания для привычек и голодания — Switch UI есть, WorkManager не подключён | feature-settings/ReminderSettingsScreen | Post-MVP S-05 |
| KI-04 | Onboarding — одноэкранный stub без multi-step flow и permission requests | feature-settings/OnboardingScreen | Post-MVP S-06 |
| KI-05 | История шагов — экран stub ("скоро") | feature-home/StepsHistoryScreen | Post-MVP ST-07 |
| KI-06 | Steps: нет синхронизации с Health Connect / Google Fit | feature-home/StepsRepository | Post-MVP (backlog) |
| KI-07 | P-ST-01 / A-ST-01 — продуктовая spec и ADR по шагам написаны ретроспективно | DECISIONS/ | Закрыто ретроспективно |

## Критичность: Low (технический долг)

| # | Описание | Файл/модуль |
|---|---|---|
| KI-08 | TODO-комментарии в коде (TODO ST-07 в StepsHistoryScreen, TODO S-03..S-06 в settings) | Несколько файлов |
| KI-09 | TodayScreen: переход к истории шагов из виджета не реализован (onHistoryClick = {}) | feature-home/TodayScreen |
| KI-10 | Нет тёмной темы | core-ui/RitmTheme |
