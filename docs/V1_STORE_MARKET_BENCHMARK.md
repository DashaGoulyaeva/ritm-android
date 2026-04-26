# V1_STORE_MARKET_BENCHMARK.md — Сверка V1 с топовыми приложениями в сторах

> Цель: проверить V1 «Ритма» не только против open-source референсов, но и против массовых приложений из Google Play / App Store.  
> Дата среза: 2026-04-25. Рынок и позиции в сторах меняются, поэтому этот документ фиксирует не абсолютный рейтинг, а рыночную планку по видимым топовым приложениям, скачиваниям, рейтингам и store listing features.

---

## Метод

Смотрел приложения, которые заметны в Google Play / App Store по категориям:
- intermittent fasting;
- water tracker / drink water reminder;
- period / cycle tracker.

В приоритете:
- store listings;
- заявленные скачивания / рейтинги;
- приложения с большим числом отзывов;
- приложения, которые повторяются в похожих выдачах и подборках.

Это не юридический список "топ-10 мира", а практический benchmark: что уже привык видеть обычный пользователь в популярных приложениях.

---

## Топовые fasting-приложения

| Приложение | Store-сигнал | Что есть важного |
|---|---|---|
| Zero | 1M+ downloads, 60K+ reviews в Google Play | fasting timer, reminders, statistics, Learn library, achievements, 16:8 / circadian / 18:6 / OMAD / 20:4 / custom fasts, Google Fit sync |
| Fastic | 10M+ downloads, 400K+ reviews в Google Play | fasting, body status tracking, water tracker, step counter, reminders, Google Fit sync, recipes, courses, AI assistant, challenges, buddies |
| BodyFast | 50M+ installs claim, 140K+ App Store ratings | 10+ customizable plans, fasting timer, reminders, fasting stages, water tracker, weight/body measurements, knowledge pool, coach, recipes, challenges |
| Simple | 5M+ downloads, top free Health & Fitness placement in Google Play | AI coach, personalized plans, nutrition guidance, routines, Google Fit/Fitbit sync for steps/weight/water/sleep |
| Window | 1M+ downloads в Google Play, Apple Watch/App Health on iOS | fasting/eating timer, reminders, weight journal, water tracker, healthy blog, Apple Health / Watch sync, mood/nutrition tracker |
| Easy Fast | visible Play listing | simple ad-free fasting tracker, water tracker, smart reminders |
| Intermittent Fasting Tracker | visible App Store listing | timer, personal profile, fasting levels, custom levels, goal timer, notifications, Apple Health sync |
| Fasting: Track fasting hours | visible Play listing | beginner fasting tracker, real-time countdown, weight-loss positioning |
| Fastyle | visible Play listing | fasting, body status, water/weight tracker |
| Fasting & Weight Loss Tracker | visible Play listing | fasting countdown, weight-loss flow, simple guided tracking |

### Fasting: рыночная планка для V1

Must-have:
- preset plans: 12/12, 14/10, 16/8, 18/6, 20/4, 23/1;
- custom fasting window;
- active fasting timer + eating window timer;
- start/stop with confirmation;
- reminders for start/end;
- fasting history with duration, plan, status;
- persistence after app restart / day change;
- simple fasting statistics: completed sessions, average duration, current streak;
- body-stage copy, but only informational and without medical promises;
- water tracker visible near fasting state;
- clear disclaimer: not medical or weight-loss guarantee.

Should-have:
- weight entry / weight trend if product decides to compete in weight-loss territory;
- challenges / achievements;
- wearable / Health Connect sync.

Do not force into V1:
- AI coach;
- recipes and meal scanner;
- community/buddies;
- aggressive weight-loss funnel.

---

## Топовые water-приложения

| Приложение | Store-сигнал | Что есть важного |
|---|---|---|
| WaterMinder | 1M+ downloads in Google Play, App Store Editors' Choice, 33K+ App Store ratings | hydration goal calculator, quick cups, custom cups, other drink types, reminders, history/graphs, awards, widgets, Wear OS / Apple Watch, Apple Health |
| Plant Nanny | 5M+ downloads, 170K+ reviews in Google Play | gamified plant growth, customized plan, reminders, charts, monthly comparisons, easy editing, rewards/missions |
| Hydro Coach | store + official site presence | hydration goals, reminders, drink tracking, watch support |
| Waterllama | App Store-focused official site | widgets, Apple Watch, Apple Health sync, 40+ beverages, challenges, characters, smart reminders |
| My Water | App Store presence | water requirement calculator, reminders, units, statistics, awards, tips, Apple Watch / Health |
| My Water - Daily Water Tracker | App Store presence | schedule-based reminders, detailed daily history, drink types, calendar completion marks, streak, widget, history editing |
| Water Tracker - Drink Reminder | App Store presence | goal calculation from body data, reminders, daily tracker |
| AquaAlert | Google Play presence | personal hydration assistant, reminders, detailed intake record, ads |
| Drink Water Reminder / Daily Tracker variants | repeated store presence | quick logging, reminders, goal tracking, units |
| Minimal hydration trackers | recurring indie/store pattern | simple local history, custom goals, no tracking, distraction-free positioning |

### Water: рыночная планка для V1

Market pattern:
- quick log in 1 tap;
- daily goal;
- goal setup from simple user choice, not necessarily medical formula;
- custom glass size or at least clear "1 glass = X ml" setting;
- water / tea / coffee and ability to add/edit drink types later;
- delete/edit mistaken entry;
- reminders with active time window;
- daily/weekly/monthly history;
- simple statistics: average glasses, goal completion, streak;
- gentle achievement/streak feedback;
- privacy-friendly wording: local data, no account.

Ritm V1 decision:
- берем quick log, цель в стаканах, историю, статистику, исправление ошибочной записи и мягкие reminders;
- не делаем custom glass size, миллилитры/унции и десятки напитков основным V1 UX;
- причина: «Ритм» не приложение для жёсткого подсчёта каждой капли, а спокойный трекер для занятых людей, которым важнее регулярность и низкая когнитивная нагрузка.

Should-have:
- widget for quick add;
- wearable support;
- gamification beyond small achievements;
- Apple Health / Health Connect sync.

Do not force into V1:
- plant/pet game loop;
- complex medical hydration calculator;
- social sharing.

---

## Топовые cycle / period-приложения

| Приложение | Store-сигнал | Что есть важного |
|---|---|---|
| Flo | 100M+ downloads, 4.6M+ reviews in Google Play | period/ovulation calendar, symptoms, discharge, mood, pregnancy, partner mode, expert content, broad lifecycle positioning |
| Clue | 50M+ downloads, 1.4M+ reviews in Google Play | period calendar, ovulation/fertility tools, PMS prediction, reminders, birth control alerts, irregular cycles, PCOS/endometriosis/perimenopause positioning, 300+ articles, custom tags, data privacy |
| Period Calendar Period Tracker | 100M+ downloads, 4.7M+ reviews in Google Play | period, flow, cycles, ovulation, conception chance, cervical mucus, BMI, sex, weight, temperature, symptoms, moods, pill/reminder flow |
| Stardust | 1M+ downloads in Google Play, 100K+ App Store ratings | period/pregnancy/hormone tracker, daily insights, PMS/cravings/mood/fertility predictions, friend/partner syncing, moon/cycle framing, privacy positioning |
| Ovia | 88K+ App Store ratings | period/fertility calendar, daily fertility score, pregnancy/postpartum/perimenopause/menopause, symptoms/moods/sex/nutrition, birth control, Apple Health/Fitbit |
| My Calendar / Period Tracker | 1M+ downloads in Google Play | period/fertility/ovulation prediction, notes, intercourse, moods, symptoms, weight, temperature, pill reminders, backup/restore, pregnancy mode |
| Period Tracker Diary | visible Google Play listing | flow, symptoms, moods, temperature, weight, sexual activity, medication, diary |
| Clover | visible Google Play listing | period/fertility log, PMS, flow amount, symptom/emotion tracking |
| Simpluna | 50K+ downloads, minimalist positioning | simple calendar, predicted next period, notes, no ads/columns/noise |
| Natural Cycles / fertility-first apps | major market presence | contraception/fertility positioning, regulated/medical expectations, not suitable for lightweight V1 without compliance strategy |

### Cycle: рыночная планка для V1

Must-have:
- period start/end logging;
- calendar with past and predicted days;
- ovulation/fertile window estimate as optional display, with careful disclaimer;
- symptoms with levels;
- flow intensity;
- mood;
- notes;
- reminders: upcoming period, log today, optional PMS/ovulation;
- cycle statistics: average cycle length, period length, last cycles;
- prediction confidence: "мало данных", "примерно", "на основе N циклов";
- edit past dates;
- privacy-first statement in onboarding/About;
- not-for-contraception / not-medical disclaimer.

Should-have:
- birth control reminder;
- custom tags;
- partner sharing;
- educational articles;
- backup/export.

Do not force into V1:
- pregnancy mode;
- TTC/fertility planning as a main product promise;
- contraception claims;
- PCOS/endometriosis/perimenopause modes;
- AI health interpretation.

---

## Cross-category market expectations

Топовые приложения почти всегда имеют:

| Ожидание | Что это значит для «Ритма» |
|---|---|
| Onboarding | Нужно быстро объяснить, зачем один Today вместо 3-4 отдельных приложений |
| Personalization | Минимум: цели, расписания, reminders, custom fasting/window/cups |
| Statistics | Без графиков/сводок пользователь не видит ценности накопленных данных |
| History editing | Люди ошибаются; публичная V1 должна давать исправлять записи |
| Reminders | Вода, привычки, fasting, цикл — все требуют понятного notification flow |
| Privacy | В health/wellness категории это часть продукта, а не юридическая сноска |
| Widgets/wearables | Не обязательно для V1, но это сильный стандарт у лидеров |
| Achievements/streaks | Нужны мягкие retention-сигналы без давления |
| Store assets | Топы продают через скриншоты, понятное описание и доверие |
| Medical disclaimers | Особенно cycle/fasting: нельзя обещать диагнозы, контрацепцию или лечение |

---

## Что меняется в V1 после store benchmark

### Поднять в Must-have

1. Custom fasting window.
2. Eating-window timer, не только fasting timer.
3. Fasting statistics: completed sessions, current streak, average duration.
4. Fasting body-stage copy, но без медицинских обещаний.
5. Water: keep glass-first UX; do not make custom ml/oz setup a V1 requirement.
6. Water: edit/delete entries.
7. Water: weekly/monthly statistics.
8. Water: active reminder window.
9. Cycle: optional ovulation/fertile window estimate with disclaimer.
10. Cycle: reminders for upcoming period and daily log.
11. Cycle: edit past dates.
12. Cycle: statistics and prediction confidence.
13. All domains: onboarding that explains privacy, reminders and data storage.
14. All domains: export data.
15. All domains: no visible broken premium/paywall/stub patterns.

### Keep out of V1 unless product direction changes

1. AI coach.
2. Calorie/meal scanner.
3. Recipes.
4. Weight-loss funnel.
5. Pregnancy/postpartum/perimenopause modes.
6. Partner/friend sharing.
7. Community/challenges as social features.
8. Health Connect outside steps or without fallback.
9. Wear OS/widget as blocker.

---

## Updated V1 promise

V1 «Ритма» должна быть не "бедной версией Flo + Zero + WaterMinder", а спокойной privacy-first альтернативой:

> один Today-экран, быстрые действия, локальные данные, без аккаунта, без рекламы, без медицинских обещаний.

Но чтобы это сработало, базовая глубина должна быть рыночной:
- fasting не просто старт/стоп, а планы, таймеры, история, статистика, reminders;
- вода не просто плюс стакан, а goal, reminders, history, edit, stats — но без ml/oz-heavy UX;
- цикл не просто дата начала, а calendar, symptoms, moods, phases, prediction confidence, reminders;
- привычки не просто чекбокс, а schedule, streak, stats, archive, reminder;
- данные не просто "в Room", а понятный privacy/export story.
