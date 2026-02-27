# Story M1.1: Engine Contract and Session Lifecycle

Status: ready-for-dev

## Story

As a J Playground engineer,
I want a shared engine contract and deterministic session lifecycle,
so that platform bindings can implement one consistent REPL runtime safely.

**Acceptance Criteria References:** M1-AC-001, M1-AC-002, M1-AC-003, M1-AC-014

## Acceptance Criteria

1. Common/shared module defines an engine contract that includes `eval`, `reset`, and `shutdown`, with a shared result model for output/error semantics. (M1-AC-001)
2. Session runtime guarantees exactly one active interpreter instance per app session. (M1-AC-002)
3. No interpreter/session data is persisted across app restarts. (M1-AC-003)
4. Session lifecycle includes deterministic shutdown behavior that safely closes engine resources. (M1-AC-014)

## Tasks / Subtasks

- [ ] Define common engine abstraction and result model in shared code. (AC: M1-AC-001)
  - [ ] Add shared interfaces/models in `composeApp/src/commonMain/kotlin/com/jsoftware/...`.
  - [ ] Ensure names and packaging align with `com.jsoftware` conventions.
- [ ] Implement session manager semantics for one interpreter instance per session. (AC: M1-AC-002)
  - [ ] Add explicit lifecycle owner for create/use/shutdown boundaries.
  - [ ] Guard against accidental multiple instance creation.
- [ ] Ensure no persistence across restart and deterministic shutdown. (AC: M1-AC-003, M1-AC-014)
  - [ ] Confirm startup initializes fresh state each run.
  - [ ] Confirm shutdown path is explicit and repeatable.
- [ ] Add tests covering contract/lifecycle expectations. (AC: M1-AC-001, M1-AC-002, M1-AC-003, M1-AC-014)

## Test Cases

| Test Case ID | Title | Coverage | AC IDs |
| --- | --- | --- | --- |
| TC-M1-001 | Shared Contract Is Enforced in Common Code | Core | M1-AC-001 |
| TC-M1-002 | Exactly One Interpreter Instance Per Session | Core | M1-AC-002 |
| TC-M1-003 | No State Persistence Across App Restart | Core | M1-AC-003 |
| TC-M1-004 | Deterministic Shutdown Including Repeat Call | Edge | M1-AC-014 |

- Detailed steps and expected results: `features/milestone-01-test-cases.md`

## Dev Notes

- Keep platform specifics out of common code; common code defines behavior contracts only.
- Use KMP shared-interface pattern so JVM binding remains replaceable by Android/iOS later.
- Lifecycle semantics should support Milestone 01 only; avoid introducing multiline or UI-specific behavior.

### Project Structure Notes

- Shared contract/model should live under common source set paths.
- JVM implementation details must stay in `jvmMain` and consume shared contract.
- No output location should depend on `_bmad-output`; this story tracks implementation expectations only.

### References

- PRD source: `features/J Playground PRD.md` Sections 3.1, 7.2, 7.3, 11, 12.
- Kotlin Multiplatform source sets: <https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project>
- Shared API boundaries: <https://kotlinlang.org/docs/multiplatform/multiplatform-connect-to-apis>
- `expect/actual` guidance: <https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual>

## Dev Agent Record

### Agent Model Used

GPT-5 Codex

### Debug Log References

- N/A (planning artifact)

### Completion Notes List

- Story drafted from Milestone 01 PRD scope and acceptance criteria catalog.

### File List

- `features/milestone-01-story-1-engine-contract-and-session-lifecycle.md`
