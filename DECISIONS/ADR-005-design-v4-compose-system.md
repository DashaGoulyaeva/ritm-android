# ADR-005 — Design v4 Compose system

## Статус

Accepted for implementation planning. Дата: 2026-04-25.

## Контекст

Design v4 задаёт единый визуальный язык для пяти ритмов приложения: cycle, fasting, water, steps, habits. Текущий Compose-код уже имеет `core-ui` как единственный общий UI-слой, Material 3 тему, `RitmSpacing`, базовые `ritmCard`/`ritmButton` и feature-экраны Today, Steps, Fasting, Habits. Эта заметка фиксирует, как переносить v4 в Compose без изменения доменных контрактов и навигации.

Источники:
- `ARCHITECTURE.md`: feature-модули не зависят друг от друга напрямую; общий UI живёт в `core-ui`.
- `DESIGN_in_progress/design_v4/tokens.jsx`: палитра, soft-токены, `RHYTHM_LEGEND`, `RhythmOrb`, `Banner`.
- `DECISIONS/DESIGN-AUDIT-004.md`: v4 принят как зрелая база, но контраст баннеров и fasting history требуют явного контроля.
- `core-ui/src/main/kotlin/com/dashagoulyaeva/ritm/core/ui/theme/*`: текущая Material 3 тема, spacing, типографика.
- `feature-home`, `feature-fasting`, `feature-habits`: существующие Compose-контракты и состояния.

## Решение

Design v4 переносится в Compose как слой дизайн-токенов и примитивов в `core-ui`, поверх Material 3, без переписывания feature-domain и без изменения публичных ViewModel/UI state контрактов. Feature-модули должны потреблять семантические токены и primitives из `core-ui`, а не копировать hex-цвета или hand-drawn правила локально.

## Mapping токенов

### 1. Five rhythm colors

| Rhythm | v4 token | Hex | Compose semantic target | Текущий код |
|---|---:|---:|---|---|
| cycle | `T.cycle` | `#D81B60` | `CycleAccent` / rhythm token `cycle` | уже есть `CycleAccent` |
| fasting | `T.fasting` | `#FF8A65` | `FastingAccent` / rhythm token `fasting` | уже есть `FastingAccent` |
| water | `T.water` | `#29B6F6` | `WaterAccent` / rhythm token `water` | уже есть `WaterAccent` |
| steps | `T.steps` | `#FDD835` | добавить rhythm token `steps` при внедрении | сейчас отдельного `StepsAccent` нет |
| habits | `T.habits` | `#66BB6A` | `HabitsAccent` / rhythm token `habits` | уже есть `HabitsAccent` и `Tertiary` |

`Primary #5C6BC0` остаётся брендовым/system action цветом, но не является одним из пяти rhythm colors.

### 2. Soft colors

Soft-токены используются для крупных светлых поверхностей, карточек-баннеров и спокойных selected states:

| Token | Hex | Назначение |
|---|---:|---|
| `cycleSoft` | `#FCE4EC` | cycle surface/banner background |
| `fastingSoft` | `#FFE5DB` | fasting surface/banner background |
| `waterSoft` | `#E1F5FE` | water surface/banner background |
| `stepsSoft` | `#FFF6BD` | основной фон крупных steps-поверхностей |
| `habitsSoft` | `#E8F5E9` | habits surface/banner background |
| `primarySoft` | `#E8EAF6` | neutral brand/system surface |

Текущий `PrimaryContainer #E8EAFF` близок, но не равен v4 `primarySoft #E8EAF6`; при внедрении v4 нужно выбрать v4 значение как source of truth. `Background #F5F5F5` и `Surface #FAFAFA` должны быть заменены планово на v4 neutrals `bg #F2EFE8`, `surface #FBFAF6`, `paper #FBF8F3`, но это отдельная production-задача, не часть этой заметки.

### 3. Neutrals

| v4 token | Hex / value | Compose target |
|---|---:|---|
| `bg` | `#F2EFE8` | `colorScheme.background` |
| `surface` | `#FBFAF6` | `colorScheme.surface` / cards |
| `paper` | `#FBF8F3` | elevated paper-like surfaces |
| `ink` | `#0F1115` | primary text / `onSurface` |
| `ink2` | `#4E5560` | secondary text / `onSurfaceVariant` |
| `ink3` | `#8C93A0` | tertiary metadata / disabled annotations |
| `line` | `#0F1115` | sketch border stroke, not every divider |
| `hair` | `rgba(15,17,21,0.12)` | subtle dividers / track strokes |

## Contrast helpers

`tokens.jsx` currently models banner contrast with `LIGHT_TONES`, but `DESIGN-AUDIT-004` found a gap: `T.fasting` and `T.habits` are used as banner tones while not listed as light. Compose must not port this as a static allow-list only.

Required Compose rule:
- Provide a contrast helper in `core-ui` when v4 is implemented, e.g. semantic equivalent of `contentColorForRhythm(tone)`.
- Prefer luminance/contrast calculation over hard-coded `LIGHT_TONES` membership.
- For normal text, choose the content color that reaches at least WCAG AA 4.5:1 where possible.
- For v4 colors, measured contrast against `ink #0F1115` vs white is:
  - `water #29B6F6`: ink `8.20`, white `2.30` → use ink.
  - `fasting #FF8A65`: ink `8.17`, white `2.31` → use ink.
  - `habits #66BB6A`: ink `7.99`, white `2.36` → use ink.
  - `steps #FDD835`: ink `13.54`, white `1.40` → use ink.
  - `primary #5C6BC0`: ink `3.89`, white `4.86` → use white for normal text.
  - `cycle #D81B60`: ink `3.82`, white `4.95` → use white for normal text.
  - all soft tokens should use ink.

Implication: `RitmBanner` must default to computed content color. Explicit overrides should be rare and documented at call site.

## Primitives

### RhythmOrb

`RhythmOrb` is the signature v4 primitive. Compose implementation should live in `core-ui`, not in a feature module.

Contract to preserve from v4:
- `state`: one of `today`, `cycle`, `fasting`, `water`, `steps`, `habits`.
- `today` renders all five rhythm rings in order: cycle, fasting, water, steps, habits.
- Single-rhythm states render one ring/arc using that rhythm color.
- `progress` is optional and overrides default rhythm progress.
- Defaults from v4: cycle `0.50`, fasting `0.58`, water `0.625`, steps `0.80`, habits `0.50`.
- Visual style: hand-drawn/rough circle, thin ink track, colored progress arc, small tick marker, centered value/label.
- Any jitter/randomness must be deterministic by seed to avoid recomposition flicker.

Compose note: implement with `Canvas`/`Path` or stable vector drawing. Do not make `RhythmOrb` depend on feature ViewModels or domain models.

### RitmBanner

`RitmBanner` is the Compose name for v4 `Banner`.

Contract to preserve from v4:
- Parameters conceptually map to `kicker`, `headline`, `meta`, `tone`, `hero`, `ornament`.
- Text color is derived by contrast helper, not by caller guessing white/ink.
- `tone` may be a rhythm color or soft color.
- Large light surfaces should normally use soft colors; saturated water/fasting/habits/steps banners must still use ink text if used.
- Banner is presentational only; navigation and feature actions remain outside via lambdas.

## Typography and spacing

### Typography

Current `RitmTypography` already follows a calmer hierarchy: mostly `Normal`, `Medium`, `SemiBold`, with no `800` weights. Keep this direction.

Mapping guidance:
- v4 `display` (`Space Grotesk`, fallback `Inter`) maps to Material headline/display roles where available. Until fonts are added, do not block implementation; use existing Material 3 typography.
- v4 `ui` (`Inter`) maps to body/title/label roles.
- Avoid adding `FontWeight.ExtraBold`/`800` for v4 screens.
- Minimum functional UI text should be `11sp`; current `labelMedium 12sp` is acceptable.
- Existing `bodyMedium 14sp`, `bodyLarge 16sp`, `titleMedium 16sp`, `titleLarge 20sp`, `headlineMedium 24sp`, `headlineLarge 28sp` can remain initial Compose scale.

### Spacing

Current `RitmSpacing` is the stable Compose spacing contract:
- `xs = 4.dp`
- `sm = 8.dp`
- `md = 16.dp`
- `lg = 24.dp`
- `xl = 32.dp`

v4 token values such as `Block pad = 14px`, `Banner padding = 20/18/18`, `Pill padding = 5/12` should be adapted to the nearest spacing scale rather than introducing many one-off values. Exceptions are allowed only inside low-level primitives (`RitmBanner`, pill/marker/orb drawing) where visual fidelity requires it.

## Contracts not to break

### Architecture and module boundaries

Do not break the architecture rule from `ARCHITECTURE.md`:
- `core-ui` must not depend on feature modules.
- feature modules may depend on `core-ui`, `core-common`, `core-database`.
- feature modules must not depend on each other directly.
- Shared v4 primitives and tokens belong in `core-ui`.

### Existing public Compose APIs

Do not rename or remove existing public composables during visual migration unless a separate migration task updates all call sites:
- `ritmTheme`
- `ritmCard`, `ritmSectionCard`
- `ritmButton`, `ritmTonalButton`, `ritmOutlinedButton`
- `todayScreen`
- `stepsTodayWidget`
- `fastingBottomSheet`, `fastingTimerWidget`, `fastingHistoryScreen`
- `habitsScreen`, `habitDetailScreen`

### Today and Steps

Do not remove the Today steps block. v4 explicitly restores steps on Today, and `TodayState`/`StepsUiState` already expose step data.

Contracts to keep:
- `TodayState.stepsToday`, `TodayState.stepsGoal` remain compatible until a deliberate model migration exists.
- `StepsUiState.todaySteps`, `dailyGoal`, `isAvailable`, `isLoading`, `error`, and computed `progress` remain valid.
- Steps fallback states must stay distinguishable enough for UI/QA: permission denied, sensor unavailable, no data/freshness. Current code has `isAvailable`/`error`; do not collapse future richer states into a single generic empty state.
- Steps visual color maps to `steps #FDD835`, but large steps cards should prefer `stepsSoft #FFF6BD` with ink text.

### Fasting

Do not remove fasting history. The v4 audit flags missing wireframe history as a blocker, while production already has `GetFastingHistory`, `FastingHistoryViewModel`, and `fastingHistoryScreen`.

Contracts to keep:
- `FastingUiState.activeSession`, `remainingMs`, `progressFraction`, `selectedPlan`, `isLoading`.
- Active timer progress remains `0f..1f`.
- Start/stop/cancel actions remain ViewModel-owned; v4 primitives remain presentational.
- Use `fasting #FF8A65` for accent arcs and active states, `fastingSoft #FFE5DB` for large surfaces. Because saturated fasting is light, banner text must be ink, not white.
- Avoid medical claims in UI copy; any autophagy/stage wording must be informational and non-promissory.

### Habits

Contracts to keep:
- Habit CRUD/check-in flows and `CheckHabitToday` semantics.
- `HabitsAccent` maps to `habits #66BB6A`.
- Interactive habit completion may remain a real Material `Checkbox` where it changes state. v4 `Marker` is not a checkbox; v4 `HabitCheck` is the correct visual analogue for habit check interactions.
- Do not replace accessible checkboxes with non-semantic drawings unless equivalent accessibility role/state/click handling is provided.

### Core UI compatibility

- Keep Material 3 as the base theme; v4 is a token/primitives layer, not a replacement UI toolkit.
- Existing `MaterialTheme.spacing` extension remains the spacing access contract.
- Existing accent constants should remain source-compatible; adding a richer `RitmColors`/`RhythmColor` model must not force immediate feature rewrites.
- Do not hard-code v4 hex values inside feature screens after token migration begins.

## Migration guidance for future implementation tasks

1. Add v4 color/soft/neutral tokens to `core-ui` behind source-compatible names.
2. Add contrast helper and use it in `RitmBanner` before adopting saturated v4 banners.
3. Add `RhythmOrb` as a pure presentational primitive with deterministic drawing.
4. Convert Today visual layout first, preserving water, habits, fasting, cycle, and steps blocks.
5. Convert Fasting and Habits screens after Today primitives are stable.
6. Keep all domain models, repositories, use cases, and ViewModels unchanged unless a separate ADR/task approves contract changes.

## Non-goals

- No production code changes in this ADR task.
- No renaming of existing composables.
- No feature-domain refactor.
- No navigation changes.
- No deletion of existing Material 3 components until v4 replacements are implemented and tested.
