# HUMAN_VERIFICATION.md - MVP manual verification checklist

> Goal: keep manual checks repeatable and comparable across APK builds.
> Format: one run = one checklist + one short report.

---

## Device prep

1. Install fresh APK (clean install for first-run checks).
2. Allow app notifications.
3. For steps scenarios, allow `ACTIVITY_RECOGNITION`.
4. Verify device time/date and timezone are correct.

---

## Mandatory smoke checks

### HV-01 First launch
1. Open app after install.
2. Confirm start screen loads with no crash.
3. Restart app 2 times.
4. Expected: no crash, state is consistent.

### HV-02 Navigation
1. Open main sections (Today, Habits, Cycle, Settings).
2. Return to Today.
3. Expected: transitions work, no broken/empty screen.

### HV-03 Water
1. Open quick log from Today.
2. Add water, tea, coffee (one each).
3. Open water history.
4. Expected: progress and history are consistent.

### HV-04 Water reminders
1. In water settings, enable reminder and set +2 min.
2. Wait for notification.
3. Disable reminder.
4. Expected: notification arrives once, no duplicates after disable.

### HV-05 Habits (after Stage 2)
1. Create habit.
2. Mark habit done for today.
3. Open habit details.
4. Expected: status/streak/history are consistent.

### HV-06 Fasting (after Stage 3)
1. Start fasting session (for example 16:8).
2. Check active timer/status.
3. Stop session.
4. Expected: history record exists and state is correct.

### HV-07 Cycle (after Stage 4)
1. Log cycle start.
2. Add day journal entry.
3. Open cycle calendar.
4. Expected: marks and journal are visible on correct date.

### HV-08 Steps (after Stage 4.5)
1. Open Today and check steps card.
2. Walk for 2-3 minutes and refresh.
3. Open steps history.
4. Expected: steps grow and daily history updates.

---

## What to record after each run

- APK version/file name + build date/time.
- Device model + Android version.
- HV results (PASS/FAIL per scenario).
- Bug list with severity (blocker/high/medium/low).
- Final decision: `Ready for next stage` or `Fix required`.
