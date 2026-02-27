# Story M1.2: JVM Engine Binding Bootstrap

Status: ready-for-dev

## Story

As a desktop JVM user of J Playground,
I want the embedded J runtime to initialize through a JVM-specific binding,
so that I can evaluate expressions without external runtime installation.

**Acceptance Criteria References:** M1-AC-004, M1-AC-005, M1-AC-006

## Acceptance Criteria

1. JVM engine binding initializes embedded J runtime successfully under normal startup conditions. (M1-AC-004)
2. Binding/bootstrap failures are surfaced through controlled error handling without crashing the app. (M1-AC-005)
3. JVM-specific engine implementation remains isolated behind shared common interfaces/contracts. (M1-AC-006)

## Tasks / Subtasks

- [ ] Implement JVM-specific engine bootstrap behind shared interface. (AC: M1-AC-004, M1-AC-006)
  - [ ] Add JVM binding implementation under `composeApp/src/jvmMain/kotlin/com/jsoftware/...`.
  - [ ] Wire runtime init path to shared session lifecycle.
- [ ] Define controlled failure handling path for bootstrap/init errors. (AC: M1-AC-005)
  - [ ] Preserve actionable error payload for REPL display layer.
  - [ ] Ensure failure does not terminate app process.
- [ ] Add validation tests for init success and failure paths. (AC: M1-AC-004, M1-AC-005, M1-AC-006)

## Test Cases

| Test Case ID | Title | Coverage | AC IDs |
| --- | --- | --- | --- |
| TC-M1-005 | JVM Runtime Bootstrap Succeeds | Core | M1-AC-004 |
| TC-M1-006 | JVM Bootstrap Failure Is Controlled | Edge | M1-AC-005 |
| TC-M1-007 | JVM Implementation Isolated Behind Shared Boundary | Core | M1-AC-006 |

- Detailed steps and expected results: `features/milestone-01-test-cases.md`

## Dev Notes

- Constrain platform code to `jvmMain`; shared code should not import JVM-specific APIs.
- Keep binding responsibilities narrow: load/init/eval/reset/shutdown bridge only.
- Do not introduce Android/iOS binding work in this story.

### Project Structure Notes

- Shared interface and models remain in `commonMain`.
- JVM binding remains in `jvmMain` with a single adapter entrypoint.
- This story should not alter mobile source sets.

### References

- PRD source: `features/J Playground PRD.md` Sections 3.7, 6.1, 10.
- KMP platform isolation via shared contracts: <https://kotlinlang.org/docs/multiplatform/multiplatform-connect-to-apis>
- KMP source-set structure: <https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project>
- `expect/actual` pattern reference: <https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual>

## Dev Agent Record

### Agent Model Used

GPT-5 Codex

### Debug Log References

- N/A (planning artifact)

### Completion Notes List

- Story drafted to isolate JVM binding bootstrap scope for Milestone 01.

### File List

- `features/milestone-01-story-2-jvm-engine-binding-bootstrap.md`
