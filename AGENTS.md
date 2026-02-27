# Repository Guidelines

## Project Structure & Module Organization
Kotlin Multiplatform Compose app targeting Android, iOS, and Desktop (JVM).
- `composeApp/` is the active Gradle module.
- `composeApp/src/commonMain/kotlin` holds shared REPL and UI logic.
- `composeApp/src/androidMain`, `iosMain`, `jvmMain` contain platform-specific bindings and entrypoints.
- `iosApp/` contains the Xcode project and Swift entrypoint.
- `features/J Playground PRD.md` is the product source of truth.

## Build, Test, and Development Commands
Use the Gradle wrapper from the repo root:
- `./gradlew :composeApp:assembleDebug` builds the Android debug app.
- `./gradlew :composeApp:run` runs the desktop JVM app.
- `./gradlew :composeApp:check` runs verification and tests.
For iOS, run from `iosApp/iosApp.xcodeproj` in Xcode.

## Product Scope & Behavioral Contracts (v1)
Keep behavior aligned with the PRD:
- Use one interpreter instance per app session; no persistence across restarts.
- Support multi-line explicit J definitions; execute only complete blocks.
- Render monospaced output with preserved whitespace and horizontal scrolling.
- Truncate output at 2,000 chars and append `[Output truncated at 2000 characters]`.
- Preserve original J error text, style errors distinctly, and never crash the interpreter.
- `Reset` must always recreate interpreter state and clear input/output immediately.
- Serialize evaluations and run engine work off the UI thread (no interrupt in v1).

## Coding Style & Naming Conventions
- Follow standard Kotlin style: 4-space indentation, clear imports, trailing commas in multiline declarations.
- Keep package naming consistent with `com.jsoftware`.
- Use `PascalCase` for types/composables, `camelCase` for functions/properties, `UPPER_SNAKE_CASE` for constants.
- Keep platform files explicit (`Platform.android.kt`, `Platform.ios.kt`, `Platform.jvm.kt`).

## Testing Guidelines
- Primary test framework is `kotlin.test`.
- Prefer shared behavior tests in `commonTest`; add platform tests only for binding/runtime differences.
- Cover PRD-critical flows: state retention, multiline completion, truncation, reset semantics, and serialized execution.
- Run verification with `./gradlew :composeApp:check` before opening a PR.

## Commit & Pull Request Guidelines
- Keep commit subjects short, direct, and imperative.
- Keep commits focused by concern (engine behavior, UI behavior, platform binding, tests).
- PRs should include: summary, affected targets (`android`, `ios`, `jvm`), test commands run, and UI screenshots/video when applicable.
- Call out PRD scope impact, especially when touching non-goals.
- Link related issues/tasks.

## Security & Configuration Tips
- Do not commit secrets or API keys in source, Gradle files, or Xcode settings.
- Keep machine-specific configuration local and document platform packaging notes in PRs (Android ABI libs, iOS static linking, desktop binaries).
