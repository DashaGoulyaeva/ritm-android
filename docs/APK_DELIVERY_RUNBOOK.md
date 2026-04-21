# APK_DELIVERY_RUNBOOK.md - AI agent APK build and report

> Goal: AI agent builds APK automatically and reports in a single format.

---

## Agent task scope

### Preconditions
- Branch is up to date (`main` or active release branch).
- Android SDK and JDK are available.
- No blocking workspace issue for build.

### Commands (Windows / PowerShell)

```powershell
.\gradlew.bat :app:assembleDebug --no-configuration-cache
.\gradlew.bat qualityCheck --no-configuration-cache
```

### Artifacts
- APK: `app/build/outputs/apk/debug/app-debug.apk`
- Optional: SHA256 checksum for APK file

---

## Required agent report template

```text
Ready, download/install/check.

Build: SUCCESS
APK: app/build/outputs/apk/debug/app-debug.apk
Checks: qualityCheck PASS/FAIL (state explicitly)
Build time: YYYY-MM-DD HH:mm (local)
Next step: run docs/HUMAN_VERIFICATION.md and send feedback.
```

---

## Failure rule

If build fails, agent must not report success and must return:
1. failing step,
2. short root cause,
3. exact next fix action.
